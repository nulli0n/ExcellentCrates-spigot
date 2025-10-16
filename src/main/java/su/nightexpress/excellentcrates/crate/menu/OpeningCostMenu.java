package su.nightexpress.excellentcrates.crate.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
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
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class OpeningCostMenu extends LinkedMenu<CratesPlugin, CrateSource> implements ConfigBased {

    private final CrateManager manager;
    private final Map<Integer, int[]> slotsByCostsAmount;

    public OpeningCostMenu(@NotNull CratesPlugin plugin, @NotNull CrateManager manager) {
        super(plugin, MenuType.GENERIC_9X3, BLACK.wrap("[" + DARK_GRAY.wrap(CRATE_NAME) + "] Select a Cost"));
        this.manager = manager;
        this.slotsByCostsAmount = new HashMap<>();
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).getCrate().replacePlaceholders().apply(super.getTitle(viewer));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        CrateSource source = this.getLink(player);
        Crate crate = source.getCrate();

        List<Cost> costs = crate.getCosts().stream().filter(Cost::isAvailable).toList();
        int costCount = costs.size();
        int[] costSlots = this.slotsByCostsAmount.getOrDefault(costCount, new int[0]);

        for (int index = 0; index < costCount; index++) {
            if (index >= costSlots.length) break;

            Cost cost = costs.get(index);
            int slot = costSlots[index];
            int maxOpenings = cost.countMaxOpenings(player);

            viewer.addItem(NightItem.fromItemStack(cost.getIconStack())
                .localized(maxOpenings > 0 ? Lang.UI_COSTS_OPTION_AVAILABLE : Lang.UI_COSTS_OPTION_UNAVAILABLE)
                .replacement(replacer -> replacer
                    .replace(crate.replacePlaceholders())
                    .replace(cost.replacePlaceholders())
                    .replace(GENERIC_COSTS, () -> formatCostEntries(cost, player))
                    .replace(GENERIC_AVAILABLE, String.valueOf(maxOpenings))
                )
                .toMenuItem()
                .setPriority(Integer.MAX_VALUE)
                .setSlots(slot)
                .setHandler((viewer1, event) -> {
                    this.runNextTick(() -> {
                        if (maxOpenings > 1 && Config.isMassOpenEnabled()) {
                            this.manager.openAmountMenu(player, source, cost);
                            return;
                        }

                        player.closeInventory();
                        this.manager.openCrate(player, source, OpenOptions.empty(), cost);
                    });
                })
                .build()
            );
        }
    }

    @NotNull
    private static String formatCostEntries(@NotNull Cost cost, @NotNull Player player) {
        return cost.getEntries().stream()
            .map(costEntry -> {
                boolean canAfford = costEntry.hasEnough(player);
                return (canAfford ? Lang.UI_COSTS_ENTRY_AVAILABLE : Lang.UI_COSTS_ENTRY_UNAVAILABLE).text().replace(Placeholders.GENERIC_ENTRY, costEntry.format());
            })
            .collect(Collectors.joining(TagWrappers.BR));
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.slotsByCostsAmount.clear();
        for (int count = 0; count < 10; count++) {
            int amount = count + 1;
            int[] defSlots = getDefaultSlots(amount);
            int[] skillSlots = ConfigValue.create("Cost.SlotsByCount." + amount, defSlots).read(config);

            this.slotsByCostsAmount.put(amount, skillSlots);
        }

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26)
        );

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(10,11,12,13,14,15,16)
        );
    }

    private static int[] getDefaultSlots(int count) {
        return switch (count) {
            case 1 -> new int[]{13};
            case 2 -> new int[]{12, 14};
            case 3 -> new int[]{11, 13, 15};
            case 4 -> new int[]{10, 12, 14, 16};
            case 5 -> new int[]{11, 12, 13, 14, 15};
            case 6 -> new int[]{10, 11, 12, 14, 15, 16};
            case 7 -> new int[]{10, 11, 12, 13, 14, 15, 16};
            default -> new int[]{};
        };
    }
}
