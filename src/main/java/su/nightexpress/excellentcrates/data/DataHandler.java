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
import su.nightexpress.excellentcrates.data.impl.CrateData;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.LimitData;
import su.nightexpress.excellentcrates.data.serialize.CrateDataSerializer;
import su.nightexpress.excellentcrates.data.serialize.LimitDataSerializer;
import su.nightexpress.nightcore.database.AbstractUserDataHandler;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.column.ColumnType;
import su.nightexpress.nightcore.database.sql.query.UpdateEntity;
import su.nightexpress.nightcore.database.sql.query.UpdateQuery;
import su.nightexpress.nightcore.util.Lists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<CratesPlugin, CrateUser> {

    private static final SQLColumn COLUMN_KEYS              = SQLColumn.of("keys", ColumnType.STRING);
    private static final SQLColumn COLUMN_KEYS_ON_HOLD      = SQLColumn.of("keysOnHold", ColumnType.STRING);

    @Deprecated private static final SQLColumn COLUMN_CRATE_COOLDOWNS   = SQLColumn.of("crateCooldowns", ColumnType.STRING);
    @Deprecated private static final SQLColumn COLUMN_CRATE_OPENINGS    = SQLColumn.of("crateOpenings", ColumnType.STRING);
    @Deprecated private static final SQLColumn COLUMN_CRATE_MILESTONES  = SQLColumn.of("crateMilestones", ColumnType.STRING);
    @Deprecated private static final SQLColumn COLUMN_REWARD_WIN_LIMITS = SQLColumn.of("rewardWinLimits", ColumnType.STRING);

    public static final SQLColumn COLUMN_CRATE_DATA = SQLColumn.of("crateData", ColumnType.STRING);

    private static final SQLColumn COLUMN_CRATE_ID    = SQLColumn.of("crateId", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_ID   = SQLColumn.of("rewardId", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_DATA = SQLColumn.of("rewardData", ColumnType.STRING);

    private final String                         rewardDataTable;
    private final Function<ResultSet, CrateUser> userFunction;
    private final Set<Reward>                    scheduledLimitSaves;

    public DataHandler(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.rewardDataTable = this.getTablePrefix() + "_reward_data";
        this.scheduledLimitSaves = ConcurrentHashMap.newKeySet();

        this.userFunction = resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                Map<String, Integer> keys = this.gson.fromJson(resultSet.getString(COLUMN_KEYS.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                Map<String, Integer> keysOnHold = this.gson.fromJson(resultSet.getString(COLUMN_KEYS_ON_HOLD.getName()), new TypeToken<Map<String, Integer>>() {}.getType());

                Map<String, CrateData> crateDataMap = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_DATA.getName()), new TypeToken<Map<String, CrateData>>(){}.getType());
                crateDataMap.keySet().removeIf(crateId -> plugin.getCrateManager().getCrateById(crateId) == null);

                return new CrateUser(plugin, uuid, name, dateCreated, lastOnline, keys, keysOnHold, crateDataMap);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };
    }

    public void update() {
        if (!SQLQueries.hasColumn(this.getConnector(), this.tableUsers, COLUMN_CRATE_COOLDOWNS)) return;

        Map<UUID, Map<String, CrateData>> map = new HashMap<>();

        Function<ResultSet, Void> function = resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));

                //Map<String, Integer> keys = this.gson.fromJson(resultSet.getString(COLUMN_KEYS.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                //Map<String, Integer> keysOnHold = this.gson.fromJson(resultSet.getString(COLUMN_KEYS_ON_HOLD.getName()), new TypeToken<Map<String, Integer>>() {}.getType());

                Map<String, Long> openCooldowns = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_COOLDOWNS.getName()), new TypeToken<Map<String, Long>>() {}.getType());
                Map<String, Integer> openingsAmount = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_OPENINGS.getName()), new TypeToken<Map<String, Integer>>(){}.getType());
                Map<String, Integer> milestones = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_MILESTONES.getName()), new TypeToken<Map<String, Integer>>(){}.getType());
                Map<String, Map<String, LimitData>> rewardWinLimits = this.gson.fromJson(resultSet.getString(COLUMN_REWARD_WIN_LIMITS.getName()), new TypeToken<Map<String, Map<String, LimitData>>>() {}.getType());

                Map<String, CrateData> dataMap = new HashMap<>();

                Set<String> crateIds = new HashSet<>();
                crateIds.addAll(openCooldowns.keySet());
                crateIds.addAll(openingsAmount.keySet());
                crateIds.addAll(milestones.keySet());
                crateIds.addAll(rewardWinLimits.keySet());
                crateIds.addAll(plugin.getCrateManager().getCrateIds(false));

                crateIds.forEach(id -> {
                    long cooldown = openCooldowns.getOrDefault(id, 0L);
                    int openings = openingsAmount.getOrDefault(id, 0);
                    int milestone = milestones.getOrDefault(id, 0);
                    var winData = rewardWinLimits.getOrDefault(id, new HashMap<>());

                    dataMap.put(id, new CrateData(cooldown, openings, milestone, winData));
                });

                if (!dataMap.isEmpty()) {
                    map.put(uuid, dataMap);
                }

                return null;
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };

        this.load(this.tableUsers, function);

        map.forEach((id, dataMap) -> {
            this.executeUpdate(this.tableUsers,
                Lists.newList(COLUMN_CRATE_DATA.toValue(this.gson.toJson(dataMap))),
                Lists.newList(SQLCondition.equal(COLUMN_USER_ID.toValue(id.toString())))
            );
        });

        this.dropColumn(this.tableUsers,
            COLUMN_CRATE_COOLDOWNS, COLUMN_CRATE_OPENINGS, COLUMN_CRATE_MILESTONES, COLUMN_REWARD_WIN_LIMITS
        );
    }

    @Override
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return super.registerAdapters(builder)
            .registerTypeAdapter(LimitData.class, new LimitDataSerializer())
            .registerTypeAdapter(CrateData.class, new CrateDataSerializer());
    }

    @Override
    public void onSynchronize() {
        for (CrateUser user : this.plugin.getUserManager().getLoaded()) {
            if (plugin.getUserManager().isScheduledToSave(user)) continue;

            // Do not sync while opening crates.
            Player player = user.getPlayer();
            if (player != null && plugin.getOpeningManager().isOpening(player)) continue;

            CrateUser fresh = this.getUser(user.getId());
            if (fresh == null) continue;
            if (!user.isSyncReady()) continue;

            user.getKeysMap().clear();
            user.getKeysMap().putAll(fresh.getKeysMap());
            user.getCrateDataMap().clear();
            user.getCrateDataMap().putAll(fresh.getCrateDataMap());
        }

        if (Config.DATABASE_SYNC_REWARDS_DATA.get()) {
            this.plugin.getCrateManager().loadRewardLimits();
        }
    }

    @Override
    public void onSave() {
        this.saveRewardWinLimits(this.scheduledLimitSaves);
        //this.plugin.debug("Saved " + this.scheduledLimitSaves.size() + " limit datas.");
        this.scheduledLimitSaves.clear();
    }

    @Override
    protected void createUserTable() {
        super.createUserTable();

        this.addColumn(this.tableUsers,
            COLUMN_KEYS_ON_HOLD.toValue("{}"),
            COLUMN_CRATE_DATA.toValue("{}")
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
        return Lists.newList(COLUMN_CRATE_DATA, COLUMN_KEYS, COLUMN_KEYS_ON_HOLD);
    }

    @Override
    @NotNull
    protected List<SQLValue> getSaveColumns(@NotNull CrateUser user) {
        return Lists.newList(
            COLUMN_CRATE_DATA.toValue(this.gson.toJson(user.getCrateDataMap())),
            COLUMN_KEYS.toValue(this.gson.toJson(user.getKeysMap())),
            COLUMN_KEYS_ON_HOLD.toValue(this.gson.toJson(user.getKeysOnHold()))
        );
    }

    @Override
    @NotNull
    protected Function<ResultSet, CrateUser> getUserFunction() {
        return this.userFunction;
    }

    public void scheduleLimitSave(@NotNull Reward reward) {
        this.scheduledLimitSaves.add(reward);
    }

    @NotNull
    public Map<String, Map<String, LimitData>> getRewardLimits() {
        Map<String, Map<String, LimitData>> map = new HashMap<>();

        Function<ResultSet, Void> function = resultSet -> {
            try {
                String crateId = resultSet.getString(COLUMN_CRATE_ID.getName());
                String rewardId = resultSet.getString(COLUMN_REWARD_ID.getName());
                LimitData limitData = this.gson.fromJson(resultSet.getString(COLUMN_REWARD_DATA.getName()), new TypeToken<LimitData>(){}.getType());

                map.computeIfAbsent(crateId, k -> new HashMap<>()).put(rewardId, limitData);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        };

        this.load(this.rewardDataTable, function);

        return map;
    }

    public void insertRewardLimitData(@NotNull Reward reward, @NotNull LimitData winData) {
        this.insert(this.rewardDataTable, Lists.newList(
            COLUMN_CRATE_ID.toValue(reward.getCrate().getId()),
            COLUMN_REWARD_ID.toValue(reward.getId()),
            COLUMN_REWARD_DATA.toValue(this.gson.toJson(winData))
        ));
    }

    public void saveRewardWinLimits(@NotNull Set<Reward> rewards) {
        List<UpdateEntity> entities = rewards.stream().map(this::createLimitUpdate).filter(Objects::nonNull).toList();
        UpdateQuery query = UpdateQuery.create(this.rewardDataTable, entities);
        this.executeUpdate(query);
    }

    @Nullable
    private UpdateEntity createLimitUpdate(@NotNull Reward reward) {
        if (reward.getGlobalLimitData() == null) return null;

        return UpdateEntity.create(
            Lists.newList(
                COLUMN_REWARD_DATA.toValue(this.gson.toJson(reward.getGlobalLimitData()))
            ),
            Lists.newList(
                SQLCondition.equal(COLUMN_CRATE_ID.toValue(reward.getCrate().getId())),
                SQLCondition.equal(COLUMN_REWARD_ID.toValue(reward.getId()))
            )
        );
    }

    public void deleteRewardLimitData(@NotNull Reward reward) {
        this.deleteRewardLimitData(reward.getCrate().getId(), reward.getId());
    }

    public void deleteRewardLimitData(@NotNull String crateId, @NotNull String rewardId) {
        this.delete(this.rewardDataTable,
            SQLCondition.equal(COLUMN_CRATE_ID.toValue(crateId)),
            SQLCondition.equal(COLUMN_REWARD_ID.toValue(rewardId)));
    }

    public void deleteRewardLimitData(@NotNull Crate crate) {
        this.deleteRewardLimitData(crate.getId());
    }

    public void deleteRewardLimitData(@NotNull String crateId) {
        this.delete(this.rewardDataTable, SQLCondition.equal(COLUMN_CRATE_ID.toValue(crateId)));
    }
}
