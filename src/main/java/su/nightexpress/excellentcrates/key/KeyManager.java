package su.nightexpress.excellentcrates.key;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.editor.type.CreationResult;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.*;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class KeyManager extends AbstractManager<CratesPlugin> {

    private final Map<String, CrateKey> keysMap;



    public KeyManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.keysMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadKeys();

        this.addListener(new KeyListener(this.plugin, this));
    }

    private void loadKeys() {
        for (File file : FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_KEYS, true)) {
            this.loadKey(new CrateKey(this.plugin, file));
        }
        this.plugin.info("Loaded " + this.keysMap.size() + " crate keys.");
    }

    private void loadKey(@NotNull CrateKey crateKey) {
        if (crateKey.load()) {
            this.keysMap.put(crateKey.getId(), crateKey);
        }
        else this.plugin.error("Key not loaded: '" + crateKey.getFile().getName() + "'.");
    }

    @Override
    protected void onShutdown() {
        this.keysMap.clear();
    }

    @NotNull
    public CreationResult create(@NotNull String id) {
        id = CrateUtils.validateId(id);
        if (id.isBlank()) return CreationResult.ERROR_NAME;
        if (this.getKeyById(id) != null) return CreationResult.ERROR_NAME;

        File file = new File(plugin.getDataFolder() + Config.DIR_KEYS, id + ".yml");
        FileUtil.create(file);

        CrateKey key = new CrateKey(this.plugin, file);
        key.setName(StringUtil.capitalizeFully(id) + " Key");
        key.setVirtual(false);

        ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(key.getName());
        });

        key.setItem(item);
        key.save();

        this.loadKey(key);
        return CreationResult.SUCCESS;
    }

    public boolean delete(@NotNull CrateKey crateKey) {
        if (crateKey.getFile().delete()) {
            this.keysMap.remove(crateKey.getId());
            return true;
        }
        return false;
    }

    public boolean spawnKey(@NotNull CrateKey key, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        world.dropItemNaturally(location, key.getItem());
        return true;
    }

    @NotNull
    public Map<String, CrateKey> getKeysMap() {
        return Collections.unmodifiableMap(this.keysMap);
    }

    @NotNull
    public Collection<CrateKey> getKeys() {
        return this.getKeysMap().values();
    }

    @NotNull
    public List<String> getKeyIds() {
        return new ArrayList<>(this.keysMap.keySet());
    }

    @Nullable
    public CrateKey getKeyById(@NotNull String id) {
        return this.keysMap.get(id.toLowerCase());
    }

    @Nullable
    public CrateKey getKeyByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getString(item, Keys.keyId).orElse(null);
        return id == null ? null : this.getKeyById(id);
    }

    @NotNull
    public Set<CrateKey> getKeys(@NotNull Player player, @NotNull Crate crate) {
        Set<CrateKey> keys = crate.getKeys();
        keys.removeIf(key -> !this.hasKey(player, key));
        return keys;
    }

    @Nullable
    public ItemStack getFirstKeyStack(@NotNull Player player, @NotNull CrateKey key) {
        Predicate<ItemStack> predicate = this.getItemStackPredicate(key);
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && !item.getType().isAir() && predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    @Nullable
    public CrateKey getFirstKey(@NotNull Player player, @NotNull Crate crate) {
        Predicate<ItemStack> predicate = itemStack -> this.isKey(itemStack, crate);
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && !item.getType().isAir() && predicate.test(item)) {
                return this.getKeyByItem(item);
            }
        }
        return null;
    }

    public boolean isKey(@NotNull ItemStack item) {
        return this.getKeyByItem(item) != null;
    }

    public boolean isKey(@NotNull ItemStack item, @NotNull CrateKey key) {
        return this.getItemStackPredicate(key).test(item);
    }

    public boolean isKey(@NotNull ItemStack item, @NotNull Crate crate) {
        CrateKey key = this.getKeyByItem(item);
        return key != null && crate.isGoodKey(key);
    }

    public int getKeysAmount(@NotNull Player player, @NotNull Crate crate) {
        return crate.getKeys().stream().mapToInt(key -> this.getKeysAmount(player, key)).sum();
    }

    public int getKeysAmount(@NotNull Player player, @NotNull CrateKey key) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            return user.getKeys(key.getId());
        }
        return Players.countItem(player, this.getItemStackPredicate(key));
    }

    public boolean hasKey(@NotNull Player player, @NotNull Crate crate) {
        return crate.getKeys().stream().anyMatch(key -> this.hasKey(player, key));
    }

    public boolean hasKey(@NotNull Player player, @NotNull CrateKey key) {
        if (key.isVirtual()) return this.getKeysAmount(player, key) > 0;

        return this.getFirstKeyStack(player, key) != null;
    }

    public void giveKeysOnHold(@NotNull Player player) {
        CrateUser user = plugin.getUserManager().getUserData(player);
        user.getKeysOnHold().forEach((keyId, amount) -> {
            CrateKey crateKey = this.getKeyById(keyId);
            if (crateKey == null) return;

            this.giveKey(player, crateKey, amount);
        });
        user.cleanKeysOnHold();
        this.plugin.getUserManager().saveAsync(user);
    }

    public void setKey(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        Player player = user.getPlayer();
        if (player != null) {
            this.setKey(player, key, amount);
            return;
        }

        if (key.isVirtual()) {
            user.setKeys(key.getId(), amount);
        }
    }

    public void setKey(@NotNull Player player, @NotNull CrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            user.setKeys(key.getId(), amount);
        }
        else {
            ItemStack keyItem = key.getItem();
            int has = Players.countItem(player, keyItem);
            if (has > amount) {
                Players.takeItem(player, keyItem, has - amount);
            }
            else if (has < amount) {
                Players.addItem(player, keyItem, amount - has);
            }
        }
        //return true;
    }

    public void giveKey(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        Player player = user.getPlayer();
        if (player != null) {
            this.giveKey(player, key, amount);
            return;
        }

        if (key.isVirtual()) {
            user.addKeys(key.getId(), amount);
        }
        else {
            user.addKeysOnHold(key.getId(), amount);
        }
    }

    public void giveKey(@NotNull Player player, @NotNull CrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            user.addKeys(key.getId(), amount);
        }
        else {
            ItemStack keyItem = key.getItem();
            keyItem.setAmount(amount < 0 ? Math.abs(amount) : amount);
            Players.addItem(player, keyItem);
        }
    }

    @Nullable
    public CrateKey takeKey(@NotNull Player player, @NotNull Crate crate) {
        CrateKey key = this.getKeys(player, crate).stream().findFirst().orElse(null);
        if (key == null) return null;

        this.takeKey(player, key, 1);
        return key;
    }

    public void takeKey(@NotNull CrateUser user, @NotNull CrateKey key, int amount) {
        Player player = user.getPlayer();
        if (player != null) {
            this.takeKey(player, key, amount);
            return;
        }

        if (key.isVirtual()) {
            user.takeKeys(key.getId(), amount);
        }
    }

    public void takeKey(@NotNull Player player, @NotNull CrateKey key, int amount) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getUserData(player);
            user.takeKeys(key.getId(), amount);
        }
        else {
            Predicate<ItemStack> predicate = this.getItemStackPredicate(key);
            int has = Players.countItem(player, predicate);
            if (has < amount) amount = has;

            Players.takeItem(player, predicate, amount);
        }
    }

    @NotNull
    private Predicate<ItemStack> getItemStackPredicate(@NotNull CrateKey key) {
        return stack -> this.getKeyByItem(stack) == key;
    }
}
