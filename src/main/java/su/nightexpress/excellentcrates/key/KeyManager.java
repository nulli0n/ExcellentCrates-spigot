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
import su.nightexpress.excellentcrates.crate.cost.type.impl.KeyCostType;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class KeyManager extends AbstractManager<CratesPlugin> {

    private final Map<String, CrateKey> keyByIdMap;

    public KeyManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.keyByIdMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadCost();
        this.loadKeys();
        this.plugin.runTask(task -> this.reportProblems()); // When everything is loaded.

        this.addListener(new KeyListener(this.plugin, this));
        this.addAsyncTask(this::saveKeys, Config.CRATE_SAVE_INTERVAL.get()); // TODO Config
    }

    @Override
    protected void onShutdown() {
        this.saveKeys();
        this.keyByIdMap.clear();
    }

    private void loadCost() {
        CratesRegistries.registerCostType(new KeyCostType(this.plugin, this));
    }

    private void loadKeys() {
        for (File file : FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_KEYS, true)) {
            String id = Strings.varStyle(FileConfig.getName(file)).orElseThrow(); // TODO Handle
            this.loadKey(new CrateKey(this.plugin, file.toPath(), id));
        }
        this.plugin.info("Loaded " + this.keyByIdMap.size() + " crate keys.");
    }

    private void loadKey(@NotNull CrateKey key) {
        try {
            key.load();
            this.keyByIdMap.put(key.getId(), key);
        }
        catch (IllegalStateException exception) {
            this.plugin.error("Key not loaded: '" + key.getPath() + "'.");
            exception.printStackTrace();
        }
    }

    private void saveKeys() {
        this.getKeys().forEach(CrateKey::saveIfDirty);
    }

    private void reportProblems() {
        this.getKeys().forEach(key -> key.collectProblems().print(this.plugin.getLogger()));
    }

    public boolean createKey(@NotNull String id) {
        Path path = Path.of(this.plugin.getDataFolder() + Config.DIR_KEYS, FileConfig.withExtension(id));
        FileUtil.createFileIfNotExists(path);

        CrateKey key = new CrateKey(this.plugin, path, id);
        key.setName(StringUtil.capitalizeFully(id) + " Key");
        key.setVirtual(false);

        ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(key.getName());
        });

        key.setItem(ItemHelper.vanilla(item));
        key.saveForce();

        this.loadKey(key);
        return true;
    }

    public boolean delete(@NotNull CrateKey key) {
        try {
            if (!Files.deleteIfExists(key.getPath())) return false;
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        this.keyByIdMap.remove(key.getId());
        return true;
    }

    public boolean dropKeyItem(@NotNull CrateKey key, @NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        world.dropItemNaturally(location, key.getItemStack());
        return true;
    }

    public int countKeys() {
        return this.keyByIdMap.size();
    }

    public boolean hasKeys() {
        return !this.keyByIdMap.isEmpty();
    }

    public boolean hasKey(@NotNull String id) {
        return this.keyByIdMap.containsKey(id);
    }

    @NotNull
    public Map<String, CrateKey> getKeyByIdMap() {
        return this.keyByIdMap;
    }

    @NotNull
    public Set<CrateKey> getKeys() {
        return new HashSet<>(this.keyByIdMap.values());
    }

    @NotNull
    public List<String> getKeyIds() {
        return new ArrayList<>(this.keyByIdMap.keySet());
    }

    @Nullable
    public CrateKey getKeyById(@NotNull String id) {
        return this.keyByIdMap.get(id.toLowerCase());
    }

    @Nullable
    public CrateKey getKeyByItem(@NotNull ItemStack item) {
        String id = PDCUtil.getString(item, Keys.keyId).orElse(null);
        return id == null ? null : this.getKeyById(id);
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

    public boolean isValidKey(@NotNull String keyId) {
        return this.getKeyById(keyId) != null;
    }

    public boolean isKey(@NotNull ItemStack item) {
        return this.getKeyByItem(item) != null;
    }

    public boolean isKey(@NotNull ItemStack item, @NotNull CrateKey key) {
        return this.getItemStackPredicate(key).test(item);
    }

    public int getKeysAmount(@NotNull Player player, @NotNull CrateKey key) {
        if (key.isVirtual()) {
            CrateUser user = plugin.getUserManager().getOrFetch(player);
            return user.countKeys(key);
        }
        return Players.countItem(player, this.getItemStackPredicate(key));
    }

    public boolean hasKey(@NotNull Player player, @NotNull CrateKey key) {
        if (key.isVirtual()) return this.getKeysAmount(player, key) > 0;

        return this.getFirstKeyStack(player, key) != null;
    }

    public void giveKeysOnHold(@NotNull Player player) {
        CrateUser user = plugin.getUserManager().getOrFetch(player);
        user.getKeysOnHold().forEach((keyId, amount) -> {
            CrateKey crateKey = this.getKeyById(keyId);
            if (crateKey == null) return;

            this.giveKey(player, crateKey, amount);
        });
        user.cleanKeysOnHold();
        this.plugin.getUserManager().save(user);
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
            CrateUser user = plugin.getUserManager().getOrFetch(player);
            user.setKeys(key.getId(), amount);
            plugin.getUserManager().save(user);
        }
        else {
            ItemStack keyItem = key.getItemStack();
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
            CrateUser user = plugin.getUserManager().getOrFetch(player);
            user.addKeys(key.getId(), amount);
            plugin.getUserManager().save(user);
        }
        else {
            ItemStack keyItem = key.getItemStack();
            keyItem.setAmount(amount < 0 ? Math.abs(amount) : amount);
            Players.addItem(player, keyItem);
        }
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
            CrateUser user = plugin.getUserManager().getOrFetch(player);
            user.takeKeys(key.getId(), amount);
            plugin.getUserManager().save(user);
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
