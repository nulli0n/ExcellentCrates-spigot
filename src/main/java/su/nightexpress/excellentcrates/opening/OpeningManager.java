package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.impl.BasicOpening;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpeningConfig;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpeningMenu;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OpeningManager extends AbstractManager<CratesPlugin> {

    private final Map<String, InventoryOpeningMenu> menuMap;
    private final Map<UUID, Opening>                openingDataMap;

    public OpeningManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.menuMap = new HashMap<>();
        this.openingDataMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.plugin.extractResources(Config.DIR_OPENINGS);

        for (FileConfig config : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_OPENINGS)) {
            InventoryOpeningMenu menu = new InventoryOpeningMenu(plugin, config);
            String id = config.getFile().getName().replace(".yml", "");
            this.menuMap.put(id.toLowerCase(), menu);
        }
        this.plugin.info("Loaded " + this.menuMap.size() + " crate openings.");

        this.addListener(new OpeningListener(this.plugin, this));

        this.addTask(this.plugin.createTask(this::tickOpenings).setTicksInterval(1L));
    }

    @Override
    protected void onShutdown() {
        this.getOpenings().forEach(Opening::emergencyStop);
    }

    @NotNull
    public Map<String, InventoryOpeningMenu> getMenuMap() {
        return menuMap;
    }

    @NotNull
    public Map<UUID, Opening> getOpeningDataMap() {
        return Collections.unmodifiableMap(this.openingDataMap);
    }

    @NotNull
    public Collection<Opening> getOpenings() {
        return this.getOpeningDataMap().values();
    }

    @Nullable
    public InventoryOpeningMenu getInventoryOpening(@NotNull String id) {
        return this.getMenuMap().get(id.toLowerCase());
    }

    @Nullable
    public Opening getOpeningData(@NotNull Player player) {
        return this.openingDataMap.get(player.getUniqueId());
    }

    public void tickOpenings() {
        this.getOpenings().forEach(Opening::tick);
    }

    public boolean isOpening(@NotNull Player player) {
        return this.getOpeningData(player) != null;
    }

    public void stopOpening(@NotNull Player player) {
        Opening opening = this.removeOpening(player);
        if (opening == null) return;

        opening.stop();
    }

    @Nullable
    public Opening removeOpening(@NotNull Player player) {
        return this.openingDataMap.remove(player.getUniqueId());
    }

    @NotNull
    public Opening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        Crate crate = source.getCrate();
        String openingName = crate.getOpeningConfig();
        if (openingName != null) {
            InventoryOpeningMenu menu = this.getInventoryOpening(openingName);
            if (menu != null) {
                InventoryOpeningConfig config = menu.getOpeningConfig();
                return new InventoryOpening(plugin, menu, config, player, source, key);
            }
        }

        return new BasicOpening(plugin, player, source, key);
    }

    public boolean startOpening(@NotNull Player player, @NotNull Opening opening, boolean instaRoll) {
        if (this.isOpening(player)) return false;

        InventoryOpening inventoryOpening = opening instanceof InventoryOpening io ? io : null;

        this.openingDataMap.putIfAbsent(player.getUniqueId(), opening);

        if (inventoryOpening != null) {
            if (!instaRoll) {
                InventoryOpeningMenu menu = inventoryOpening.getMenu();
                if (menu.isViewer(player) || !menu.open(player) || player.getOpenInventory().getType() == InventoryType.CRAFTING) {
                    menu.close(player);
                    return false;
                }
            }
        }

        if (inventoryOpening != null) {
            InventoryOpeningMenu menu = inventoryOpening.getMenu();
            Inventory inventory;

            if (!instaRoll) {
                /*MenuViewer viewer = menu.getViewer(player);
                if (viewer != null) {
                    menu.close();
                }

                if (!menu.open(player)) return false;*/
                inventory = player.getOpenInventory().getTopInventory();
                //if (inventory.getType() == InventoryType.CRAFTING) return false;
            }
            else {
                inventory = menu.getOptions().createInventory();
            }

            inventoryOpening.setInventory(inventory);
            inventoryOpening.postOpen();

            if (instaRoll) opening.instaRoll();
        }
        else {
            opening.run();
            if (instaRoll) {
                opening.instaRoll();
            }
        }

        return true;
    }

    /*@Nullable
    @Deprecated
    public Opening startOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key, boolean simulate) {
        if (this.isOpening(player)) return null;

        Opening opening = this.createOpening(player, source, key);
        this.openingDataMap.put(player.getUniqueId(), opening);

        if (opening instanceof InventoryOpening inventoryOpening) {
            InventoryOpeningMenu menu = inventoryOpening.getMenu();
            Inventory inventory;

            if (!simulate) {
                if (!menu.open(player)) return null;

                inventory = player.getOpenInventory().getTopInventory();
            }
            else {
                inventory = menu.getOptions().createInventory();
            }

            inventoryOpening.setInventory(inventory);
            inventoryOpening.onOpen();

            if (simulate) opening.instaRoll();
        }
        else {
            if (simulate) {
                opening.instaRoll();
            }
            else {
                opening.start();
            }
        }

        opening.setSaveData(!simulate);

        return opening;
    }*/
}
