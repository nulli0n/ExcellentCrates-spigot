package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardListMenu extends LinkedMenu<CratesPlugin, Crate> implements Filled<Reward>, LangContainer {

    private static final IconLocale LOCALE_REWARD = LangEntry.iconBuilder("Editor.Button.Rewards.Reward")
        .rawName(REWARD_NAME)
        .appendCurrent("Status", GENERIC_INSPECTION)
        .appendCurrent("ID", REWARD_ID)
        .appendCurrent("Weight", REWARD_WEIGHT + " → " + GREEN.wrap(REWARD_ROLL_CHANCE + "%"))
        .appendCurrent("Rarity", REWARD_RARITY_NAME + " → " + GREEN.wrap(REWARD_RARITY_ROLL_CHANCE + "%"))
        .br()
        .appendClick("Left-Click to edit")
        .appendClick("Shift-Left to move forward")
        .appendClick("Shift-Right move backward")
        .build();

    private static final IconLocale LOCALE_CREATION = LangEntry.iconBuilder("Editor.Button.Rewards.Creation")
        .accentColor(GREEN)
        .name("Reward Creation")
        .appendInfo("Drop item on " + GREEN.wrap("this") + " button", "to create a new reward of it.")
        .build();

    private static final IconLocale LOCALE_SORTING = LangEntry.iconBuilder("Editor.Button.Rewards.Sorting")
        .name("Sort Rewards")
        .appendInfo("Automatically sorts rewards in", "certain order.").br()
        .appendClick("Click to open")
        .build();

    public RewardListMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_LIST.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));

        this.addItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setSlots(IntStream.range(0, 36).toArray())
            .setPriority(-1)
        );

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setSlots(IntStream.range(36, 45).toArray())
            .setPriority(-1)
        );

        this.addItem(Material.ANVIL, LOCALE_CREATION, 42, (viewer, event, crate) -> {
            Player player = viewer.getPlayer();
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            CrateDialogs.REWARD_CREATION.ifPresent(dialog -> {
                ItemStack copy = new ItemStack(cursor);
                event.getView().setCursor(null);
                Players.addItem(player, copy);
                dialog.show(player, crate, copy, () -> this.flush(player));
            });
        });

        this.addItem(Material.COMPARATOR, LOCALE_SORTING, 38, (viewer, event, crate) -> {
            Player player = viewer.getPlayer();
            CrateDialogs.REWARD_SORTING.ifPresent(dialog -> dialog.show(player, crate, () -> this.flush(player)));
        });
    }

    @Override
    @NotNull
    public MenuFiller<Reward> createFiller(@NotNull MenuViewer viewer) {
        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getLink(viewer).getRewards());
        autoFill.setItemCreator(reward -> {
            return NightItem.fromItemStack(reward.getPreviewItem())
                .hideAllComponents()
                .localized(LOCALE_REWARD)
                .replacement(replacer -> replacer
                    .replace(GENERIC_INSPECTION, () -> Lang.inspection(Lang.INSPECTIONS_GENERIC_OVERVIEW, !reward.hasProblems()))
                    .replace(reward.replacePlaceholders())
                );
        });

        autoFill.setItemClick(reward -> (viewer1, event) -> {
            Crate crate = this.getLink(viewer1);

            if (event.isShiftClick()) {
                // Reward position move.
                List<Reward> all = new ArrayList<>(crate.getRewards());
                int index = all.indexOf(reward);
                int allSize = all.size();

                if (event.isLeftClick()) {
                    if (index + 1 >= allSize) return;

                    all.remove(index);
                    all.add(index + 1, reward);
                }
                else if (event.isRightClick()) {
                    if (index == 0) return;

                    all.remove(index);
                    all.add(index - 1, reward);
                }
                crate.setRewards(all);
                crate.markDirty();
                this.runNextTick(() -> this.flush(viewer));
                return;
            }

            if (event.isLeftClick()) {
                this.runNextTick(() -> plugin.getEditorManager().openRewardOptions(viewer1.getPlayer(), reward));
            }
        });

        return autoFill.build();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        if (result.isInventory()) {
            event.setCancelled(false);
        }
    }
}
