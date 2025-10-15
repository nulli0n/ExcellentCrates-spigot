package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CostOptionsMenu extends LinkedMenu<CratesPlugin, CostOptionsMenu.Data> implements LangContainer {

    private static final IconLocale LOCALE_OPTION_NAME = LangEntry.iconBuilder("Editor.CostOptions.Name")
        .name("Name")
        .appendCurrent("Current", COST_NAME).br()
        .appendClick("Click to change")
        .build();

    private static final IconLocale LOCALE_OPTION_ICON = LangEntry.iconBuilder("Editor.CostOptions.Icon")
        .name("Icon")
        .appendCurrent("Custom Items", GENERIC_STATE).br()
        .appendInfo("Drag and drop an item here", "to replace the icon.").br()
        .appendClick("Right-Click to toggle")
        .build();

    private static final IconLocale LOCALE_ENTRY_VACANT = LangEntry.iconBuilder("Editor.CostOptions.VacantEntry")
        .accentColor(GREEN)
        .name("Vacant Slot")
        .appendInfo("A vacant slot for cost entry.").br()
        .appendClick("Click to create")
        .build();

    private static final int[] ENTRY_SLOTS = {29,30,31,32,33};

    public record Data(@NotNull Crate crate, @NotNull Cost cost) {}

    private boolean itemDetection;

    public CostOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_COST.text());
        this.plugin.injectLang(this);
        this.itemDetection = true;

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openCosts(viewer.getPlayer(), this.getLink(viewer).crate));
        }));

        this.addItem(NightItem.fromType(Material.GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(20,21,22,23,24)
        );

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

    public void open(@NotNull Player player, @NotNull Crate crate, @NotNull Cost cost) {
        this.open(player, new Data(crate, cost));
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(viewer);
        Cost cost = data.cost;
        Crate crate = data.crate;
        List<CostEntry> entries = cost.getEntries();
        Runnable saveAndFlush = () -> {
            crate.markDirty();
            this.flush(player);
        };

        viewer.addItem(NightItem.fromType(Material.NAME_TAG)
            .localized(LOCALE_OPTION_NAME)
            .replacement(replacer -> replacer.replace(cost.replacePlaceholders()))
            .toMenuItem()
            .setSlots(12)
            .setHandler((viewer1, event) -> CrateDialogs.COST_NAME.ifPresent(dialog -> dialog.show(player, cost, saveAndFlush)))
            .build()
        );

        viewer.addItem(NightItem.fromItemStack(cost.getIconStack())
            .hideAllComponents()
            .localized(LOCALE_OPTION_ICON)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(this.itemDetection)))
            .toMenuItem()
            .setSlots(14)
            .setHandler((viewer1, event) -> {
                if (event.isRightClick()) {
                    this.itemDetection = !this.itemDetection;
                    this.runNextTick(() -> this.flush(viewer));
                    return;
                }

                ItemStack cursor = event.getCursor();
                if (cursor == null || cursor.getType().isAir()) return;

                AdaptedItem item = ItemHelper.adapt(cursor, this.itemDetection);
                cost.setIcon(item);
                crate.markDirty();
                Players.addItem(player, cursor);
                event.getView().setCursor(null);
                this.runNextTick(() -> this.flush(viewer));
            })
            .build()
        );

        for (int index = 0; index < ENTRY_SLOTS.length; index++) {
            int slot = ENTRY_SLOTS[index];
            MenuItem.Builder builder;

            if (index < entries.size()) {
                CostEntry entry = entries.get(index);

                builder = entry.getEditorIcon().toMenuItem().setHandler((viewer1, event) -> {
                    if (event.isLeftClick()) {
                        entry.openEditor(player, crate::markDirty);
                    }
                    else if (event.getClick() == ClickType.DROP) {
                        cost.removeEntry(entry);
                        crate.markDirty();
                        this.runNextTick(() -> this.flush(viewer));
                    }
                });
            }
            else {
                builder = NightItem.fromType(Material.LIME_DYE)
                    .localized(LOCALE_ENTRY_VACANT)
                    .toMenuItem()
                    .setHandler((viewer1, event) -> CrateDialogs.COST_ENTRY_CREATION.ifPresent(dialog -> dialog.show(player, cost, saveAndFlush)));
            }

            viewer.addItem(builder.setSlots(slot).setPriority(Integer.MAX_VALUE).build());
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
