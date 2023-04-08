package su.nightexpress.excellentcrates.data;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.sql.SQLColumn;
import su.nexmedia.engine.api.data.sql.SQLValue;
import su.nexmedia.engine.api.data.sql.column.ColumnType;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.data.impl.UserRewardData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class DataHandler extends AbstractUserDataHandler<ExcellentCrates, CrateUser> {

    private static final SQLColumn COLUMN_KEYS              = SQLColumn.of("keys", ColumnType.STRING);
    private static final SQLColumn COLUMN_KEYS_ON_HOLD      = SQLColumn.of("keysOnHold", ColumnType.STRING);
    private static final SQLColumn COLUMN_CRATE_COOLDOWNS   = SQLColumn.of("crateCooldowns", ColumnType.STRING);
    private static final SQLColumn COLUMN_CRATE_OPENINGS    = SQLColumn.of("crateOpenings", ColumnType.STRING);
    private static final SQLColumn COLUMN_REWARD_WIN_LIMITS = SQLColumn.of("rewardWinLimits", ColumnType.STRING);

    private static DataHandler                    instance;
    private final  Function<ResultSet, CrateUser> userFunction;

    protected DataHandler(@NotNull ExcellentCrates plugin) {
        super(plugin, plugin);

        this.userFunction = (resultSet) -> {
            try {
                UUID uuid = UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName()));
                String name = resultSet.getString(COLUMN_USER_NAME.getName());
                long dateCreated = resultSet.getLong(COLUMN_USER_DATE_CREATED.getName());
                long lastOnline = resultSet.getLong(COLUMN_USER_LAST_ONLINE.getName());

                Map<String, Integer> keys = this.gson.fromJson(resultSet.getString(COLUMN_KEYS.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                Map<String, Integer> keysOnHold = this.gson.fromJson(resultSet.getString(COLUMN_KEYS_ON_HOLD.getName()), new TypeToken<Map<String, Integer>>() {}.getType());
                Map<String, Long> openCooldowns = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_COOLDOWNS.getName()), new TypeToken<Map<String, Long>>() {}.getType());
                Map<String, Integer> openingsAmount = this.gson.fromJson(resultSet.getString(COLUMN_CRATE_OPENINGS.getName()), new TypeToken<Map<String, Integer>>(){}.getType());
                Map<String, Map<String, UserRewardData>> rewardWinLimits = this.gson.fromJson(resultSet.getString(COLUMN_REWARD_WIN_LIMITS.getName()), new TypeToken<Map<String, Map<String, UserRewardData>>>() {}.getType());

                if (openingsAmount == null) openingsAmount = new HashMap<>();

                return new CrateUser(plugin, uuid, name, dateCreated, lastOnline,
                    keys, keysOnHold, openCooldowns, openingsAmount, rewardWinLimits);
            }
            catch (SQLException e) {
                return null;
            }
        };
    }

    @NotNull
    public static DataHandler getInstance(@NotNull ExcellentCrates plugin) throws SQLException {
        if (instance == null) {
            instance = new DataHandler(plugin);
        }
        return instance;
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
        instance = null;
    }

    @Override
    public void onSynchronize() {
        for (CrateUser user : this.plugin.getUserManager().getUsersLoaded()) {
            CrateUser fresh = this.getUser(user.getId());
            if (fresh == null) continue;

            user.getKeysMap().clear();
            user.getKeysMap().putAll(fresh.getKeysMap());
            user.getCrateCooldowns().clear();
            user.getCrateCooldowns().putAll(fresh.getCrateCooldowns());
            user.getOpeningsAmountMap().clear();
            user.getOpeningsAmountMap().putAll(fresh.getOpeningsAmountMap());
            user.getRewardWinLimits().clear();
            user.getRewardWinLimits().putAll(fresh.getRewardWinLimits());
        }
    }

    @Override
    protected void createUserTable() {
        super.createUserTable();

        this.addColumn(this.tableUsers,
            COLUMN_KEYS_ON_HOLD.toValue("{}"),
            COLUMN_CRATE_COOLDOWNS.toValue("{}"),
            COLUMN_CRATE_OPENINGS.toValue("{}"),
            COLUMN_REWARD_WIN_LIMITS.toValue("{}"));
    }

    @Override
    @NotNull
    protected List<SQLColumn> getExtraColumns() {
        return Arrays.asList(
            COLUMN_KEYS, COLUMN_KEYS_ON_HOLD, COLUMN_CRATE_COOLDOWNS, COLUMN_CRATE_OPENINGS, COLUMN_REWARD_WIN_LIMITS
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
            COLUMN_REWARD_WIN_LIMITS.toValue(this.gson.toJson(user.getRewardWinLimits()))
        );
    }

    @Override
    @NotNull
    protected Function<ResultSet, CrateUser> getFunctionToUser() {
        return this.userFunction;
    }
}
