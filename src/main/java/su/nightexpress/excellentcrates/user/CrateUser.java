package su.nightexpress.excellentcrates.user;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.db.AbstractUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateUser extends AbstractUser {

    private final Map<String, Integer>       keys;
    private final Map<String, Integer>       keysOnHold;
    private final Map<String, UserCrateData> crateDataMap;

    public CrateUser(@NotNull UUID uuid, @NotNull String name) {
        this(uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public CrateUser(@NotNull UUID uuid,
                     @NotNull String name,
                     long dateCreated,
                     long lastOnline,
                     @NotNull Map<String, Integer> keys,
                     @NotNull Map<String, Integer> keysOnHold,
                     @NotNull Map<String, UserCrateData> crateDataMap) {
        super(uuid, name, dateCreated, lastOnline);
        this.keys = keys;
        this.keysOnHold = keysOnHold;
        this.crateDataMap = new HashMap<>(crateDataMap);
    }

    @NotNull
    public Map<String, UserCrateData> getCrateDataMap() {
        return this.crateDataMap;
    }

    @NotNull
    public UserCrateData getCrateData(@NotNull Crate crate) {
        return this.getCrateData(crate.getId());
    }

    @NotNull
    public UserCrateData getCrateData(@NotNull String id) {
        return this.crateDataMap.computeIfAbsent(id.toLowerCase(), k -> new UserCrateData());
    }

    @NotNull
    public Map<String, Integer> getKeysMap() {
        return this.keys;
    }

    @NotNull
    public Map<String, Integer> getKeysOnHold() {
        return this.keysOnHold;
    }

    public void addKeys(@NotNull String id, int amount) {
        this.setKeys(id, this.countKeys(id) + amount);
    }

    public void takeKeys(@NotNull String id, int amount) {
        this.addKeys(id, -amount);
    }

    public void setKeys(@NotNull String id, int amount) {
        this.keys.put(id.toLowerCase(), Math.max(0, amount));
    }

    @Deprecated
    public int getKeys(@NotNull String id) {
        return this.countKeys(id);
    }

    public boolean hasKeys(@NotNull CrateKey key) {
        return this.hasKeys(key.getId());
    }

    public boolean hasKeys(@NotNull String id) {
        return this.countKeys(id) > 0;
    }

    public int countKeys(@NotNull CrateKey key) {
        return this.countKeys(key.getId());
    }

    public int countKeys(@NotNull String id) {
        return this.keys.getOrDefault(id.toLowerCase(), 0);
    }

    public void addKeysOnHold(@NotNull String id, int amount) {
        this.keysOnHold.put(id.toLowerCase(), Math.max(0, this.getKeysOnHold(id) + amount));
    }

    public int getKeysOnHold(@NotNull String id) {
        return this.keysOnHold.getOrDefault(id.toLowerCase(), 0);
    }

    public void cleanKeysOnHold() {
        this.keysOnHold.clear();
    }
}
