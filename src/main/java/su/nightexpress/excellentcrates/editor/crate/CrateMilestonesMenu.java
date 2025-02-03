package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class CrateMilestonesMenu extends LinkedMenu<CratesPlugin, Crate> implements Filled<Milestone> {

    public CrateMilestonesMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_CRATE_MILESTONES.getString());

        this.addItem(MenuItem.buildReturn(this, 39, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));

        this.addItem(Material.ANVIL, EditorLang.MILESTONE_CREATE, 41, (viewer, event, crate) -> {
            Milestone milestone = new Milestone(crate, "null", 0);
            crate.getMilestones().add(milestone);
            crate.saveMilestones();
            this.runNextTick(() -> this.flush(viewer));
        });
    }

    @Override
    @NotNull
    public MenuFiller<Milestone> createFiller(@NotNull MenuViewer viewer) {
        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getLink(viewer).getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList());
        autoFill.setItemCreator(milestone -> {
            Reward reward = milestone.getReward();
            return NightItem.fromItemStack(reward == null ? ItemUtil.getSkinHead(Placeholders.SKIN_QUESTION_MARK) : reward.getPreviewItem())
                .localized(EditorLang.MILESTONE_OBJECT)
                .setHideComponents(true)
                .replacement(replacer -> replacer.replace(milestone.replacePlaceholders()));
        });
        autoFill.setItemClick(milestone -> (viewer1, event) -> {
            Crate crate = this.getLink(viewer1);

            if (event.isShiftClick()) {
                if (event.isRightClick()) {
                    crate.getMilestones().remove(milestone);
                    crate.saveMilestones();
                    this.runNextTick(() -> this.flush(viewer));
                    return;
                }
            }

            if (event.isLeftClick()) {
                this.handleInput(Dialog.builder(viewer1, Lang.EDITOR_ENTER_AMOUNT, input -> {
                    milestone.setOpenings(input.asInt(0));
                    crate.saveMilestones();
                    return true;
                }));
            }
            else if (event.isRightClick()) {
                this.handleInput(Dialog.builder(viewer1, Lang.EDITOR_ENTER_REWARD_ID, input -> {
                    milestone.setRewardId(input.getTextRaw());
                    crate.saveMilestones();
                    return true;
                }).setSuggestions(crate.getRewardIds(), true));
            }
        });

        return autoFill.build();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
