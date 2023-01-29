package su.nightexpress.excellentcrates.data;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.api.data.DataTypes;
import su.nightexpress.excellentcrates.ExcellentCrates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class CrateUserData extends AbstractUserDataHandler<ExcellentCrates, CrateUser> {

    private static final String COL_KEYS              = "keys";
    private static final String COL_KEYS_ONHOLD       = "keysOnHold";
    private static final String COL_CRATE_COOLDOWNS   = "crateCooldowns";
    private static final String COL_CRATE_OPENINGS    = "crateOpenings";
    private static final String COL_REWARD_WIN_LIMITS = "rewardWinLimits";

    private static CrateUserData instance;
    private final Function<ResultSet, CrateUser> FUNC_USER;

    protected CrateUserData(@NotNull ExcellentCrates plugin) {
        super(plugin, plugin);

        this.FUNC_USER = (rs) -> {
            try {
                UUID uuid = UUID.fromString(rs.getString(COL_USER_UUID));
                String name = rs.getString(COL_USER_NAME);
                long dateCreated = rs.getLong(COL_USER_DATE_CREATED);
                long lastOnline = rs.getLong(COL_USER_LAST_ONLINE);

                Map<String, Integer> keys = this.gson.fromJson(rs.getString(COL_KEYS), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Integer> keysOnHold = this.gson.fromJson(rs.getString(COL_KEYS_ONHOLD), new TypeToken<Map<String, Integer>>() {
                }.getType());
                Map<String, Long> openCooldowns = gson.fromJson(rs.getString(COL_CRATE_COOLDOWNS), new TypeToken<Map<String, Long>>() {
                }.getType());
                Map<String, Integer> openingsAmount = gson.fromJson(rs.getString(COL_CRATE_OPENINGS), new TypeToken<Map<String, Integer>>(){}.getType());
                Map<String, Map<String, UserRewardWinLimit>> rewardWinLimits = this.gson.fromJson(rs.getString(COL_REWARD_WIN_LIMITS), new TypeToken<Map<String, Map<String, UserRewardWinLimit>>>() {
                }.getType());

                return new CrateUser(plugin, uuid, name, dateCreated, lastOnline,
                    keys, keysOnHold, openCooldowns, openingsAmount, rewardWinLimits);
            }
            catch (SQLException e) {
                return null;
            }
        };
    }

    @NotNull
    public static CrateUserData getInstance(@NotNull ExcellentCrates plugin) throws SQLException {
        if (instance == null) {
            instance = new CrateUserData(plugin);
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
    protected void onTableCreate() {
        super.onTableCreate();
        this.addColumn(this.tableUsers, COL_KEYS_ONHOLD, DataTypes.STRING.build(this.getDataType()), "{}");
        this.addColumn(this.tableUsers, COL_CRATE_COOLDOWNS, DataTypes.STRING.build(this.getDataType()), "{}");
        this.addColumn(this.tableUsers, COL_CRATE_OPENINGS, DataTypes.STRING.build(this.getDataType()), "{}");
        this.addColumn(this.tableUsers, COL_REWARD_WIN_LIMITS, DataTypes.STRING.build(this.getDataType()), "{}");
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToCreate() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_KEYS, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_KEYS_ONHOLD, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_CRATE_COOLDOWNS, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_CRATE_OPENINGS, DataTypes.STRING.build(this.getDataType()));
        map.put(COL_REWARD_WIN_LIMITS, DataTypes.STRING.build(this.getDataType()));
        return map;
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToSave(@NotNull CrateUser user) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(COL_KEYS, this.gson.toJson(user.getKeysMap()));
        map.put(COL_KEYS_ONHOLD, this.gson.toJson(user.getKeysOnHold()));
        map.put(COL_CRATE_COOLDOWNS, this.gson.toJson(user.getCrateCooldowns()));
        map.put(COL_CRATE_OPENINGS, this.gson.toJson(user.getOpeningsAmountMap()));
        map.put(COL_REWARD_WIN_LIMITS, this.gson.toJson(user.getRewardWinLimits()));
        return map;
    }

    @Override
    @NotNull
    protected Function<ResultSet, CrateUser> getFunctionToUser() {
        return this.FUNC_USER;
    }
}
