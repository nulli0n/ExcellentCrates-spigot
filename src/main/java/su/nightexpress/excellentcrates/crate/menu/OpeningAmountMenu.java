package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.OpenOptions;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class OpeningAmountMenu extends LinkedMenu<CratesPlugin, OpeningAmountMenu.Data> implements ConfigBased {

    public record Data(@NotNull CrateSource source, @Nullable Cost cost) {}

    private final CrateManager manager;

    private int[] slotsSingle;
    private int[] slotsAll;

    public OpeningAmountMenu(@NotNull CratesPlugin plugin, @NotNull CrateManager manager) {
        super(plugin, MenuType.GENERIC_9X3, BLACK.wrap("[" + CRATE_NAME + "] Select Amount"));
        this.manager = manager;
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).source.getCrate().replacePlaceholders().apply(super.getTitle(viewer));
    }

    public void open(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        this.open(player, new Data(source, cost));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        CrateSource source = data.source;
        Cost cost = data.cost;
        Crate crate = source.getCrate();

        int limit = Config.MASS_OPENING_LIMIT.get();
        int canOpen = cost == null ? limit : cost.countMaxOpenings(player);

        int[] amounts = {1, canOpen};
        int[][] slots = {this.slotsSingle, this.slotsAll};

        for (int index = 0; index < amounts.length; index++) {
            int amount = amounts[index];

            viewer.addItem(NightItem.fromItemStack(crate.getItemStack())
                .localized(index == 0 ? Lang.UI_OPEN_AMOUNT_SINGLE : Lang.UI_OPEN_AMOUNT_ALL)
                .replacement(replacer -> replacer
                    .replace(crate.replacePlaceholders())
                    .replace(GENERIC_MAX, () -> String.valueOf(amount))
                )
                .setAmount(amount)
                .hideAllComponents()
                .toMenuItem()
                .setSlots(slots[index])
                .setPriority(Integer.MAX_VALUE)
                .setHandler((viewer1, event) -> {
                    this.runNextTick(() -> {
                        player.closeInventory();
                        this.manager.multiOpenCrate(player, source, OpenOptions.empty(), cost, amount);
                    });
                })
                .build()
            );
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.slotsSingle = ConfigValue.create("Slots.Single", new int[]{11}).read(config);
        this.slotsAll = ConfigValue.create("Slots.All", new int[]{15}).read(config);

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(1,2,3,10,11,12,19,20,21,5,6,7,14,15,16,23,24,25)
        );

        loader.addDefaultItem(NightItem.fromType(Material.LIME_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(4,13,22)
        );

        loader.addDefaultItem(NightItem.fromType(Material.YELLOW_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(0,9,18,8,17,26)
        );
    }
}
