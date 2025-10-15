package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.List;
import java.util.stream.Collectors;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CostsListMenu extends LinkedMenu<CratesPlugin, Crate> implements LangContainer {

    private static final int[] COST_SLOTS = {19,20,21,22,23,24,25};
    private static final int[] TOGGLE_SLOTS = {10,11,12,13,14,15,16};
    private static final int[] STATUS_SLOTS = {28,29,30,31,32,33,34};

    private static final IconLocale LOCALE_DATA_VACANT = LangEntry.iconBuilder("Editor.Costs.Data.Vacant")
        .accentColor(SOFT_AQUA)
        .name("Vacant Slot")
        .appendInfo("Add an cost option for your crate!").br()
        .appendClick("Click to create")
        .build();

    private static final IconLocale LOCALE_DATA_FILLED = LangEntry.iconBuilder("Editor.Costs.Data.Filled")
        .rawName(YELLOW.and(BOLD).wrap("Cost Option: ") + WHITE.wrap(COST_NAME) + " " + GRAY.wrap("(ID: " + WHITE.wrap(COST_ID) + ")"))
        .rawLore(ITALIC.and(DARK_GRAY).wrap("Press " + SOFT_RED.wrap(TagWrappers.KEY.apply("key.drop")) + " key to delete.")).br()
        .rawLore(GENERIC_COSTS).br()
        .appendClick("Click to edit", YELLOW)
        .build();

    private static final IconLocale LOCALE_DATA_EMPTY = LangEntry.iconBuilder("Editor.Costs.Data.Empty")
        .rawName(RED.and(BOLD).wrap("Cost Option: ") + WHITE.wrap(COST_NAME) + " " + GRAY.wrap("(ID: " + WHITE.wrap(COST_ID) + ")"))
        .rawLore(ITALIC.and(DARK_GRAY).wrap("Press " + SOFT_RED.wrap(TagWrappers.KEY.apply("key.drop")) + " key to delete.")).br()
        .rawLore(SOFT_RED.wrap("No costs defined!")).br()
        .appendClick("Click to edit", RED)
        .build();

    private static final IconLocale LOCALE_TOGGLE_ENABLED = LangEntry.iconBuilder("Editor.Costs.Toggle.Enabled")
        .accentColor(GREEN).name("Enabled").appendClick("Click to toggle").build();

    private static final IconLocale LOCALE_TOGGLE_DISABLED = LangEntry.iconBuilder("Editor.Costs.Toggle.Disabled")
        .accentColor(WHITE).name("Disabled").appendClick("Click to toggle").build();

    private static final IconLocale LOCALE_TOGGLE_NOTHING = LangEntry.iconBuilder("Editor.Costs.Toggle.Nothing")
        .rawName(GRAY.wrap("< No Data >")).build();

    private static final IconLocale LOCALE_STATUS_GOOD = LangEntry.iconBuilder("Editor.Costs.Status.Good")
        .rawName(GREEN.and(BOLD).wrap("Status: ") + WHITE.wrap("Good")).appendInfo("All entries are valid!").build();

    private static final IconLocale LOCALE_STATUS_WARN = LangEntry.iconBuilder("Editor.Costs.Status.Warn")
        .rawName(YELLOW.and(BOLD).wrap("Status: ") + WHITE.wrap("Warning")).appendInfo("Some entries are invalid!").build();

    private static final IconLocale LOCALE_STATUS_BAD = LangEntry.iconBuilder("Editor.Costs.Status.Bad")
        .rawName(RED.and(BOLD).wrap("Status: ") + WHITE.wrap("Invalid")).appendInfo("No valid entries!").build();

    private static final IconLocale LOCALE_STATUS_NOTHING = LangEntry.iconBuilder("Editor.Costs.Status.Nothing")
        .rawName(GRAY.and(BOLD).wrap("Status: ") + WHITE.wrap("< No Data>")).build();

    public CostsListMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_COSTS.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(45,46,52,53)
        );

        this.addItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(47,48,49,50,51)
        );
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        Crate crate = this.getLink(player);
        List<Cost> costs = crate.getCosts();

        for (int index = 0; index < COST_SLOTS.length; index++) {
            int slot = COST_SLOTS[index];
            int toggleSlot = TOGGLE_SLOTS[index];
            int statusSlot = STATUS_SLOTS[index];

            MenuItem.Builder entryBuilder;
            MenuItem.Builder toggleBuilder;
            MenuItem.Builder statusBuilder;

            if (index < costs.size()) {
                Cost cost = costs.get(index);
                boolean isValid = cost.isValid();
                boolean hasInvalids = cost.hasInvalids();

                toggleBuilder = NightItem.fromType(cost.isEnabled() ? Material.LIME_DYE : Material.GRAY_DYE)
                    .localized(cost.isEnabled() ? LOCALE_TOGGLE_ENABLED : LOCALE_TOGGLE_DISABLED)
                    .toMenuItem()
                    .setHandler((viewer1, event) -> {
                        cost.setEnabled(!cost.isEnabled());
                        crate.markDirty();
                        this.runNextTick(() -> this.flush(viewer));
                    });

                entryBuilder = NightItem.fromItemStack(cost.getIconStack())
                    .localized(cost.isEmpty() ? LOCALE_DATA_EMPTY : LOCALE_DATA_FILLED)
                    .replacement(replacer -> replacer
                        .replace(cost.replacePlaceholders())
                        .replace(GENERIC_COSTS, () -> cost.getEntries().stream()
                            .map(entry -> entry.isValid() ? CoreLang.goodEntry(entry.format()) : CoreLang.badEntry(entry.format()))
                            .collect(Collectors.joining(TagWrappers.BR)))
                    )
                    .hideAllComponents()
                    .toMenuItem()
                    .setHandler((viewer1, event) -> {
                        if (event.getClick() == ClickType.DROP) {
                            crate.removeCost(cost);
                            crate.markDirty();
                            this.runNextTick(() -> this.flush(viewer));
                            return;
                        }

                        this.runNextTick(() -> plugin.getEditorManager().openCostOptions(player, crate, cost));
                    });

                statusBuilder = NightItem.fromType(!isValid ? Material.RED_STAINED_GLASS_PANE : (hasInvalids ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE))
                    .localized(!isValid ? LOCALE_STATUS_BAD : (hasInvalids ? LOCALE_STATUS_WARN : LOCALE_STATUS_GOOD))
                    .toMenuItem();
            }
            else {
                toggleBuilder = NightItem.fromType(Material.GRAY_DYE).localized(LOCALE_TOGGLE_NOTHING).toMenuItem();
                statusBuilder = NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE).localized(LOCALE_STATUS_NOTHING).toMenuItem();

                entryBuilder = NightItem.fromType(Material.STRUCTURE_VOID)
                    .localized(LOCALE_DATA_VACANT)
                    .hideAllComponents()
                    .toMenuItem()
                    .setHandler((viewer1, event) -> {
                        CrateDialogs.COST_CREATION.ifPresent(dialog -> dialog.show(player, crate, () -> this.flush(player)));
                    });
            }

            viewer.addItem(entryBuilder.setSlots(slot).setPriority(Integer.MAX_VALUE).build());
            viewer.addItem(toggleBuilder.setSlots(toggleSlot).setPriority(Integer.MAX_VALUE).build());
            viewer.addItem(statusBuilder.setSlots(statusSlot).setPriority(Integer.MAX_VALUE).build());
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
