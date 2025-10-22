package su.nightexpress.excellentcrates.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.legacy.LegacyCrateData;
import su.nightexpress.excellentcrates.data.legacy.LegacyLimitData;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.data.serialize.UserCrateDataSerializer;
import su.nightexpress.excellentcrates.data.legacy.LegacyCrateDataSerializer;
import su.nightexpress.excellentcrates.data.legacy.LegacyLimitDataSerializer;
import su.nightexpress.excellentcrates.data.reward.RewardData;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.nightcore.db.AbstractUserDataManager;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.column.ColumnType;
import su.nightexpress.nightcore.db.sql.query.SQLQueries;
import su.nightexpress.nightcore.db.sql.query.impl.SelectQuery;
import su.nightexpress.nightcore.db.sql.query.type.ValuedQuery;
import su.nightexpress.nightcore.util.Lists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataManager<CratesPlugin, CrateUser> {

    public static final Column COLUMN_KEYS         = Column.of("keys", ColumnType.STRING);
    public static final Column COLUMN_KEYS_ON_HOLD = Column.of("keysOnHold", ColumnType.STRING);
    public static final Column COLUMN_CRATE_DATA   = Column.of("crateData", ColumnType.STRING);

    public static final Column COLUMN_CRATE_ID    = Column.of("crateId", ColumnType.STRING);
    public static final Column COLUMN_REWARD_ID   = Column.of("rewardId", ColumnType.STRING);
    public static final Column COLUMN_HOLDER      = Column.of("holder", ColumnType.STRING);
    public static final Column COLUMN_AMOUNT      = Column.of("amount", ColumnType.STRING);
    public static final Column COLUMN_RESET_DATE  = Column.of("resetDate", ColumnType.STRING);

    public static final Column COLUMN_LATEST_OPENER_ID   = Column.of("latestOpenerId", ColumnType.STRING);
    public static final Column COLUMN_LATEST_OPENER_NAME = Column.of("latestOpenerName", ColumnType.STRING);
    public static final Column COLUMN_LATEST_REWARD_ID   = Column.of("latestRewardId", ColumnType.STRING);

    private final String tableRewardLimits;
    private final String tableCrateData;

    public DataHandler(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.tableRewardLimits = this.getTablePrefix() + "_reward_limits";
        this.tableCrateData = this.getTablePrefix() + "_crate_data";
    }

    public void updateRewardLimits() {
        Column columnRewardData = Column.of("rewardData", ColumnType.STRING);
        String rewardDataTable = this.getTablePrefix() + "_reward_data";

        if (!SQLQueries.hasTable(this.connector, rewardDataTable)) return;
        if (!SQLQueries.hasColumn(this.connector, rewardDataTable, columnRewardData)) return;

        Function<ResultSet, List<RewardData>> playerLimitLoader = resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                Map<String, LegacyCrateData> crateDataMap = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_DATA.getName()), new TypeToken<Map<String, LegacyCrateData>>(){}.getType());

                List<RewardData> limits = new ArrayList<>();
                crateDataMap.forEach((crateId, crateData) -> {
                    crateData.getRewardDataMap().forEach((rewardId, rewardData) -> {
                        limits.add(new RewardData(crateId, rewardId, uuid.toString(), rewardData.getAmount(), rewardData.getExpireDate()));
                    });
                });
                //limits.removeIf(RewardData::isResetTime);

                return limits;
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };

        Function<ResultSet, RewardData> globalLimitLoader = resultSet -> {
            try {
                String crateId = resultSet.getString(COLUMN_CRATE_ID.getName());
                String rewardId = resultSet.getString(COLUMN_REWARD_ID.getName());
                LegacyLimitData limitData = this.gson.fromJson(resultSet.getString(columnRewardData.getName()), new TypeToken<LegacyLimitData>(){}.getType());

                return new RewardData(crateId, rewardId, crateId, limitData.getAmount(), limitData.getExpireDate());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        };

        this.select(this.tableUsers, playerLimitLoader, SelectQuery::all).forEach(limits -> {
            limits.forEach(this::insertRewardLimit);
        });

        this.select(rewardDataTable, globalLimitLoader, SelectQuery::all).forEach(this::insertRewardLimit);

        this.dropColumn(rewardDataTable, columnRewardData);
    }

    @Override
    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return builder
            .registerTypeAdapter(LegacyLimitData.class, new LegacyLimitDataSerializer())
            .registerTypeAdapter(LegacyCrateData.class, new LegacyCrateDataSerializer())
            .registerTypeAdapter(UserCrateData.class, new UserCrateDataSerializer());
    }

    @Override
    @NotNull
    protected Function<ResultSet, CrateUser> createUserFunction() {
        return resultSet -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                Map<String, Integer> keys = this.gson.fromJson(resultSet.getString(COLUMN_KEYS.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                Map<String, Integer> keysOnHold = this.gson.fromJson(resultSet.getString(COLUMN_KEYS_ON_HOLD.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                Map<String, UserCrateData> crateDataMap = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_DATA.getName()), new TypeToken<Map<String, UserCrateData>>(){}.getType());

                return new CrateUser(uuid, name, dateCreated, lastOnline, keys, keysOnHold, crateDataMap);
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                return null;
            }
        };
    }

    @Override
    public void onSynchronize() {
        for (CrateUser user : this.plugin.getUserManager().getLoaded()) {
            if (user.isAutoSavePlanned()) continue;
            if (!user.isAutoSyncReady()) continue;

            // Do not sync while opening crates.
            Player player = user.getPlayer();
            if (player != null && plugin.getOpeningManager().isOpening(player)) continue;

            CrateUser fresh = this.getUser(user.getId());
            if (fresh == null) continue;

            user.getKeysMap().clear();
            user.getKeysMap().putAll(fresh.getKeysMap());
            user.getCrateDataMap().clear();
            user.getCrateDataMap().putAll(fresh.getCrateDataMap());
        }

        this.plugin.getDataManager().handleSynchronization();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.createTable(this.tableCrateData, Lists.newList(
            COLUMN_CRATE_ID,
            COLUMN_LATEST_OPENER_ID,
            COLUMN_LATEST_OPENER_NAME,
            COLUMN_LATEST_REWARD_ID
        ));

        this.createTable(this.tableRewardLimits, Lists.newList(
            COLUMN_HOLDER,
            COLUMN_CRATE_ID,
            COLUMN_REWARD_ID,
            COLUMN_AMOUNT,
            COLUMN_RESET_DATE
        ));
    }

    @Override
    protected void addUpsertQueryData(@NotNull ValuedQuery<?, CrateUser> query) {
        query.setValue(COLUMN_CRATE_DATA, user -> this.gson.toJson(user.getCrateDataMap()));
        query.setValue(COLUMN_KEYS, user -> this.gson.toJson(user.getKeysMap()));
        query.setValue(COLUMN_KEYS_ON_HOLD, user -> this.gson.toJson(user.getKeysOnHold()));
    }

    @Override
    protected void addSelectQueryData(@NotNull SelectQuery<CrateUser> query) {
        query.column(COLUMN_CRATE_DATA);
        query.column(COLUMN_KEYS);
        query.column(COLUMN_KEYS_ON_HOLD);
    }

    @Override
    protected void addTableColumns(@NotNull List<Column> columns) {
        columns.add(COLUMN_CRATE_DATA);
        columns.add(COLUMN_KEYS);
        columns.add(COLUMN_KEYS_ON_HOLD);
    }

    @NotNull
    public List<GlobalCrateData> loadCrateDatas() {
        SelectQuery<GlobalCrateData> query = new SelectQuery<>(DataQueries.CRATE_DATA_LOADER).all();

        return this.select(this.tableCrateData, query);
    }

    public void insertCrateData(@NotNull GlobalCrateData data) {
        this.insert(this.tableCrateData, DataQueries.CRATE_DATA_INSERT, data);
    }

    public void updateCrateData(@NotNull GlobalCrateData data) {
        this.updateCrateDatas(Lists.newSet(data));
    }

    public void updateCrateDatas(@NotNull Set<GlobalCrateData> datas) {
        this.update(this.tableCrateData, DataQueries.CRATE_DATA_UPDATE, datas);
    }

    public void deleteCrateData(@NotNull GlobalCrateData data) {
        this.delete(this.tableCrateData, DataQueries.CRATE_DATA_DELETE, data);
    }

    public void deleteCrateData(@NotNull Crate crate) {
        this.delete(this.tableCrateData, DataQueries.CRATE_DATA_DELETE_CRATE, crate);
    }



    @NotNull
    public List<RewardData> loadRewardLimits() {
        SelectQuery<RewardData> query = new SelectQuery<>(DataQueries.REWARD_LIMIT_LOADER).all();

        return this.select(this.tableRewardLimits, query);
    }

    public void insertRewardLimit(@NotNull RewardData limit) {
        this.insert(this.tableRewardLimits, DataQueries.REWARD_LIMIT_INSERT, limit);
    }

    public void updateRewardLimit(@NotNull RewardData limit) {
        this.updateRewardLimits(Lists.newSet(limit));
    }

    public void updateRewardLimits(@NotNull Set<RewardData> limits) {
        this.update(this.tableRewardLimits, DataQueries.REWARD_LIMIT_UPDATE, limits);
    }

    public void deleteRewardLimit(@NotNull RewardData limit) {
        this.delete(this.tableRewardLimits, DataQueries.REWARD_LIMIT_DELETE, limit);
    }

    public void deleteRewardLimits(@NotNull Crate crate) {
        this.delete(this.tableRewardLimits, DataQueries.REWARD_LIMIT_DELETE_CRATE, crate);
    }

    public void deleteRewardLimits(@NotNull Reward reward) {
        this.delete(this.tableRewardLimits, DataQueries.REWARD_LIMIT_DELETE_REWARD, reward);
    }

    public void deleteRewardLimits(@NotNull UUID playerId) {
        this.delete(this.tableRewardLimits, DataQueries.REWARD_LIMIT_DELETE_PLAYER, playerId);
    }
}
