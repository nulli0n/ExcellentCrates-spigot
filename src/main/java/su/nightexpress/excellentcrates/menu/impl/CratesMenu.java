package su.nightexpress.excellentcrates.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.ClickType;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemOptions;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CratesMenu extends ConfigMenu<CratesPlugin> {

    private String               crateName;
    private List<String>         crateLore;
    private Map<String, Integer> crateSlots;

    public CratesMenu(@NotNull CratesPlugin plugin, @NotNull FileConfig config) {
        super(plugin, config);

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.create(item).readMeta().replacePlaceholderAPI(viewer.getPlayer()).writeMeta();
        }));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        this.crateSlots.forEach((crateId, slot) -> {
            Crate crate = plugin.getCrateManager().getCrateById(crateId);
            if (crate == null) {
                this.plugin.error("Invalid crate '" + crateId + "' in '" + this.cfg.getFile().getName() + "' menu!");
                return;
            }

            Player player = viewer.getPlayer();
            ItemStack icon = crate.getRawItem();

            ItemReplacer.create(icon).readMeta().trimmed().hideFlags()
                .setDisplayName(this.crateName)
                .setLore(this.crateLore)
                .replace(crate.replacePlaceholders())
                .replace(GENERIC_KEYS, () -> NumberUtil.format(plugin.getKeyManager().getKeysAmount(player, crate)))
                .writeMeta();

            MenuItem menuItem = new MenuItem(icon);
            menuItem.setSlots(slot);
            menuItem.setOptions(ItemOptions.personalWeak(player));
            menuItem.setHandler((viewer1, event) -> {
                ClickType clickType = ClickType.from(event);
                InteractType clickAction = Config.getCrateClickAction(clickType);
                if (clickAction == null) return;

                this.runNextTick(() -> {
                    player.closeInventory();
                    plugin.getCrateManager().interactCrate(player, crate, clickAction, null, null);
                });
            });

            this.addItem(menuItem);
        });
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Crates"), 27,  InventoryType.CHEST);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        return new ArrayList<>();
    }

    @Override
    protected void loadAdditional() {
        this.crateName = ConfigValue.create("Crate.Name",
            CRATE_NAME
        ).read(cfg);

        this.crateLore = ConfigValue.create("Crate.Lore", Lists.newList(
            LIGHT_GRAY.enclose("You have " + LIGHT_YELLOW.enclose(GENERIC_KEYS) + " keys."),
            "",
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Left-Click to " + LIGHT_YELLOW.enclose("preview") + "."),
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Right-Click to " + LIGHT_YELLOW.enclose("open") + ".")
        )).read(cfg);

        this.crateSlots = ConfigValue.forMap("Crate.Slots",
            (cfg, path, id) -> cfg.getInt(path + "." + id),
            (cfg, path, map) -> map.forEach((id, slot) -> cfg.set(path + "." + id, slot)),
            () -> Map.of("your_crate_id", 13),
            "Put here crate IDs and slots where they will be displayed.",
            "Crate IDs are equals to their config file names."
        ).read(cfg);
    }


}
