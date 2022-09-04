package su.nightexpress.excellentcrates.key;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.data.CrateUser;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KeyManager extends AbstractManager<ExcellentCrates> {

    private Map<String, ICrateKey> keysMap;

    public KeyManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.keysMap = new HashMap<>();
        this.plugin.getConfigManager().extract(Config.DIR_KEYS);

        for (JYML cfgLegacy : JYML.loadAll(plugin.getDataFolder().getParentFile() + "/GoldenCrates/keys/", true)) {
            File exist = new File(plugin.getDataFolder() + Config.DIR_KEYS + cfgLegacy.getFile().getName());
            if (exist.exists()) {
                plugin.error("Could not convert '" + cfgLegacy.getFile().getName() + "': Such key already exist!");
                continue;
            }

            CrateKey keyLegacy = CrateKey.fromLegacy(cfgLegacy);
            keyLegacy.save();
            plugin.info("Converted '" + cfgLegacy.getFile().getName() + "' Golden Crate key!");
        }

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_KEYS, true)) {
            try {
                ICrateKey crateKey = new CrateKey(plugin, cfg);
                this.keysMap.put(crateKey.getId(), crateKey);
            }
            catch (Exception ex) {
                plugin.error("Could not load '" + cfg.getFile().getName() + "' crate key!");
                ex.printStackTrace();
            }
        }
        this.plugin.info("Loaded " + this.keysMap.size() + " crate keys.");

        this.addListener(new KeyListener(this));
    }

    @Override
    public void onShutdown() {
        if (this.keysMap != null) {
            this.keysMap.values().forEach(ICrateKey::clear);
            this.keysMap.clear();
            this.keysMap = null;
        }
    }

    public boolean create(@NotNull String id) {
        if (this.getKeyById(id) != null) {
            return false;
        }

        ICrateKey crateKey = new CrateKey(plugin, id);
        crateKey.save();
        this.getKeysMap().put(crateKey.getId(), crateKey);
        return true;
    }

    public boolean delete(@NotNull ICrateKey crateKey) {
        if (crateKey.getFile().delete()) {
            crateKey.clear();
            this.getKeysMap().remove(crateKey.getId());
            return true;
        }
        return false;
    }

    @NotNull
    public Map<String, ICrateKey> getKeysMap() {
        return this.keysMap;
    }

    @NotNull
    public Collection<ICrateKey> getKeys() {
        return this.getKeysMap().values();
    }

    @NotNull
    public List<String> getKeyIds() {
        return new ArrayList<>(this.getKeysMap().keySet());
    }

    @Nullable
    public ICrateKey getKeyById(@NotNull String id) {
        return this.getKeysMap().get(id.toLowerCase());
    }

    @Nullable
    public ICrateKey getKeyByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getStringData(item, Keys.CRATE_KEY_ID);
        if (id == null) {
            id = PDCUtil.getStringData(item, Keys.OLD_CRATES_KEY_ID);
        }
        return id == null ? null : this.getKeyById(id);
    }

    @NotNull
    public Set<ICrateKey> getKeys(@NotNull ICrate crate) {
        return crate.getKeyIds().stream().map(this::getKeyById).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @NotNull
    public Set<ICrateKey> getKeys(@NotNull Player player, @NotNull ICrate crate) {
        return this.getKeys(crate).stream().filter(key -> this.getKeysAmount(player, key) > 0).collect(Collectors.toSet());
    }

    @Nullable
    public ItemStack getFirstKeyStack(@NotNull Player player, @NotNull ICrateKey crateKey) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) continue;

            ICrateKey crateKey2 = this.getKeyByItem(item);
            if (crateKey2 != null && crateKey2.equals(crateKey)) {
                return item;
            }
        }
        return null;
    }

    public boolean isKey(@NotNull ItemStack item) {
        return this.getKeyByItem(item) != null;
    }

    public int getKeysAmount(@NotNull Player player, @NotNull ICrate crate) {
        return this.getKeys(player, crate).stream().mapToInt(key -> this.getKeysAmount(player, key)).sum();
    }

    public int getKeysAmount(@NotNull Player player, @NotNull ICrateKey crateKey) {
        if (crateKey.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            return user.getKeys(crateKey.getId());
        }
        return PlayerUtil.countItem(player, itemHas -> {
            ICrateKey itemKey = this.getKeyByItem(itemHas);
            return itemKey != null && itemKey.getId().equalsIgnoreCase(crateKey.getId());
        });
    }

    public boolean hasKey(@NotNull Player player, @NotNull ICrate crate) {
        return !this.getKeys(player, crate).isEmpty();
    }

    public boolean hasKey(@NotNull Player player, @NotNull ICrateKey crateKey) {
        return this.getKeysAmount(player, crateKey) > 0;
    }

    public void giveKeysOnHold(@NotNull Player player) {
        CrateUser user = plugin.getUserManager().getUserData(player);
        user.getKeysOnHold().forEach((keyId, amount) -> {
            ICrateKey crateKey = this.getKeyById(keyId);
            if (crateKey == null) return;

            this.giveKey(player, crateKey, amount);
        });
        user.cleanKeysOnHold();
    }

    public boolean setKey(@NotNull String pName, @NotNull ICrateKey key, int amount) {
        CrateUser user = plugin.getUserManager().getUserData(pName);
        if (user == null) return false;

        Player player = user.getPlayer();
        if (player != null) {
            this.setKey(player, key, amount);
            return true;
        }

        if (key.isVirtual()) {
            user.setKeys(key.getId(), amount);
            return true;
        }
        return false;
    }

    public void setKey(@NotNull Player player, @NotNull ICrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            user.setKeys(key.getId(), amount);
        }
        else {
            ItemStack keyItem = key.getItem();
            int has = PlayerUtil.countItem(player, keyItem);
            if (has > amount) {
                PlayerUtil.takeItem(player, keyItem, has - amount);
            }
            else if (has < amount) {
                PlayerUtil.addItem(player, keyItem, amount - has);
            }
        }
        //return true;
    }

    public boolean giveKey(@NotNull String pName, @NotNull ICrateKey key, int amount) {
        CrateUser user = plugin.getUserManager().getUserData(pName);
        if (user == null) return false;

        Player player = user.getPlayer();
        if (player != null) {
            this.giveKey(player, key, amount);
            return true;
        }

        if (key.isVirtual()) {
            user.addKeys(key.getId(), amount);
        }
        else {
            user.addKeysOnHold(key.getId(), amount);
        }
        return true;
    }

    public void giveKey(@NotNull Player player, @NotNull ICrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            user.addKeys(key.getId(), amount);
        }
        else {
            ItemStack keyItem = key.getItem();
            keyItem.setAmount(amount < 0 ? Math.abs(amount) : amount);
            PlayerUtil.addItem(player, keyItem);
        }
        //return true;
    }

    public boolean takeKey(@NotNull String pName, @NotNull ICrateKey key, int amount) {
        CrateUser user = plugin.getUserManager().getUserData(pName);
        if (user == null) return false;

        Player player = user.getPlayer();
        if (player != null) {
            this.takeKey(player, key, amount);
            return true;
        }

        if (key.isVirtual()) {
            user.takeKeys(key.getId(), amount);
            return true;
        }
        return false;
    }

    public void takeKey(@NotNull Player player, @NotNull ICrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            //if (user.getKeys(key.getId()) < amount) return false;
            user.takeKeys(key.getId(), amount);
        }
        else {
            Predicate<ItemStack> predicate = itemHas -> {
                ICrateKey itemKey = this.getKeyByItem(itemHas);
                return itemKey != null && itemKey.getId().equalsIgnoreCase(key.getId());
            };
            int has = PlayerUtil.countItem(player, predicate);
            if (has < amount) amount = has;

            PlayerUtil.takeItem(player, predicate, amount);
        }
        //return true;
    }
}
