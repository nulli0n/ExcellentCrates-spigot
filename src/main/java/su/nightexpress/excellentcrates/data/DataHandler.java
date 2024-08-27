package su.nightexpress.excellentcrates.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.RewardWinData;
import su.nightexpress.excellentcrates.data.serialize.RewardWinDataSerializer;
import su.nightexpress.nightcore.database.AbstractUserDataHandler;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.column.ColumnType;
import su.nightexpress.nightcore.util.Lists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<CratesPlugin, CrateUser> {

    private static final SQLColumn COLUMN_KEYS = SQLColumn.of("keys", ColumnType.STRING);
    private static final SQLColumn COLUMN_KEYS_ON_HOLD = SQLColumn.of("keysOnHold", ColumnType.STRING);
    private static final SQLColumn COLUMN_CRATE_COOLDOWNS = SQLColumn.of("crateCooldowns", ColumnType.STRING);
    private static final SQLColumn COLUMN_CRATE_OPENINGS = SQLColumn.of("crateOpenings", ColumnType.STRING);
    private static final SQLColumn COLUMN_CRATE_MILESTONES = SQLColumn.of("crateMilestones", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_WIN_LIMITS = SQLColumn.of("rewardWinLimits", ColumnType.STRING);

    private static final SQLColumn COLUMN_CRATE_ID = SQLColumn.of("crateId", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_ID = SQLColumn.of("rewardId", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_DATA = SQLColumn.of("rewardData", ColumnType.STRING);

    private final String rewardDataTable;

    private final Function<ResultSet, CrateUser> userFunction;
    private final Function<ResultSet, RewardWinData> winDataFunction;

    public DataHandler(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.rewardDataTable = this.getTablePrefix() + "_reward_data";

        this.userFunction = (resultSet) -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                Map<String, Integer> keys = this.gson.fromJson(resultSet.getString(COLUMN_KEYS.getName()), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Integer> keysOnHold = this.gson.fromJson(resultSet.getString(COLUMN_KEYS_ON_HOLD.getName()), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Long> openCooldowns = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_COOLDOWNS.getName()), new TypeToken<Map<String, Long>>() {
                }.getType());
                Map<String, Integer> openingsAmount = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_OPENINGS.getName()), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Integer> milestones = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_MILESTONES.getName()), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Map<String, RewardWinData>> rewardWinLimits = this.gson.fromJson(resultSet.getString(COLUMN_REWARD_WIN_LIMITS.getName()), new TypeToken<Map<String, Map<String, RewardWinData>>>() {
                }.getType());

                if (openingsAmount == null) openingsAmount = new HashMap<>();
                if (milestones == null) milestones = new HashMap<>();

                openCooldowns.keySet().removeIf(crateId -> plugin.getCrateManager().getCrateById(crateId) == null);
                rewardWinLimits.keySet().removeIf(crateId -> plugin.getCrateManager().getCrateById(crateId) == null);

                return new CrateUser(plugin, uuid, name, dateCreated, lastOnline,
                        keys, keysOnHold, openCooldowns, openingsAmount, milestones, rewardWinLimits);
            } catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };

        this.winDataFunction = resultSet -> {
            try {
                return this.gson.fromJson(resultSet.getString(COLUMN_REWARD_DATA.getName()), new TypeToken<RewardWinData>() {
                }.getType());
            } catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };
    }

    @Override
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return super.registerAdapters(builder)
                .registerTypeAdapter(RewardWinData.class, new RewardWinDataSerializer());
    }

    @Override
    public void onSynchronize() {
        for (CrateUser user : this.plugin.getUserManager().getLoaded()) {
            if (plugin.getUserManager().isScheduledToSave(user)) continue;

            // Do not sync while opening crates.
            Player player = user.getPlayer();
            if (player != null && plugin.getOpeningManager().isOpening(player)) continue;

            CrateUser fresh = this.getUser(user.getId());
            if (fresh == null || !fresh.isSyncReady()) continue;

            user.getKeysMap().clear();
            user.getKeysMap().putAll(fresh.getKeysMap());
            user.getCrateCooldowns().clear();
            user.getCrateCooldowns().putAll(fresh.getCrateCooldowns());
            user.getOpeningsAmountMap().clear();
            user.getOpeningsAmountMap().putAll(fresh.getOpeningsAmountMap());
            user.getRewardWinLimits().clear();
            user.getRewardWinLimits().putAll(fresh.getRewardWinLimits());
            user.getMilestonesMap().clear();
            user.getMilestonesMap().putAll(fresh.getMilestonesMap());
        }

        if (Config.DATABASE_SYNC_REWARDS_DATA.get()) {
            for (Crate crate : this.plugin.getCrateManager().getCrates()) {
                crate.loadRewardWinDatas();
            }
        }
    }

    @Override
    protected void createUserTable() {
        super.createUserTable();

        this.addColumn(this.tableUsers,
                COLUMN_KEYS_ON_HOLD.toValue("{}"),
                COLUMN_CRATE_COOLDOWNS.toValue("{}"),
                COLUMN_CRATE_OPENINGS.toValue("{}"),
                COLUMN_REWARD_WIN_LIMITS.toValue("{}"),
                COLUMN_CRATE_MILESTONES.toValue("{}")
        );

        this.createTable(this.rewardDataTable, Lists.newList(
                COLUMN_CRATE_ID,
                COLUMN_REWARD_ID,
                COLUMN_REWARD_DATA
        ));
    }

    @Override
    @NotNull
    protected List<SQLColumn> getExtraColumns() {
        return Arrays.asList(
                COLUMN_KEYS, COLUMN_KEYS_ON_HOLD,
                COLUMN_CRATE_COOLDOWNS, COLUMN_CRATE_OPENINGS, COLUMN_CRATE_MILESTONES,
                COLUMN_REWARD_WIN_LIMITS
        );
    }

    @Override
    @NotNull
    protected List<SQLValue> getSaveColumns(@NotNull CrateUser user) {
        return Arrays.asList(
                COLUMN_KEYS.toValue(this.gson.toJson(user.getKeysMap())),
                COLUMN_KEYS_ON_HOLD.toValue(this.gson.toJson(user.getKeysOnHold())),
                COLUMN_CRATE_COOLDOWNS.toValue(this.gson.toJson(user.getCrateCooldowns())),
                COLUMN_CRATE_OPENINGS.toValue(this.gson.toJson(user.getOpeningsAmountMap())),
                COLUMN_CRATE_MILESTONES.toValue(this.gson.toJson(user.getMilestonesMap())),
                COLUMN_REWARD_WIN_LIMITS.toValue(this.gson.toJson(new HashMap<>(user.getRewardWinLimits())))
        );
    }

    @Override
    @NotNull
    protected Function<ResultSet, CrateUser> getUserFunction() {
        return this.userFunction;
    }

    @Nullable
    public RewardWinData getRewardWinData(@NotNull Reward reward) {
        return this.load(this.rewardDataTable, this.winDataFunction, Collections.emptyList(), Lists.newList(
                SQLCondition.equal(COLUMN_CRATE_ID.toValue(reward.getCrate().getId())),
                SQLCondition.equal(COLUMN_REWARD_ID.toValue(reward.getId())))
        ).orElse(null);
    }

    public void addRewardWinData(@NotNull Reward reward, @NotNull RewardWinData winData) {
        this.insert(this.rewardDataTable, Lists.newList(
                COLUMN_CRATE_ID.toValue(reward.getCrate().getId()),
                COLUMN_REWARD_ID.toValue(reward.getId()),
                COLUMN_REWARD_DATA.toValue(this.gson.toJson(winData))
        ));
    }

    public void saveRewardWinData(@NotNull Reward reward, @NotNull RewardWinData winData) {
        this.update(this.rewardDataTable, Lists.newList(
                        COLUMN_REWARD_DATA.toValue(this.gson.toJson(winData))
                ),
                SQLCondition.equal(COLUMN_CRATE_ID.toValue(reward.getCrate().getId())),
                SQLCondition.equal(COLUMN_REWARD_ID.toValue(reward.getId())));
    }

    public void deleteRewardWinData(@NotNull Reward reward) {
        this.delete(this.rewardDataTable,
                SQLCondition.equal(COLUMN_CRATE_ID.toValue(reward.getCrate().getId())),
                SQLCondition.equal(COLUMN_REWARD_ID.toValue(reward.getId())));
    }

    public void deleteRewardWinData(@NotNull Crate crate) {
        this.delete(this.rewardDataTable, SQLCondition.equal(COLUMN_CRATE_ID.toValue(crate.getId())));
    }
}
