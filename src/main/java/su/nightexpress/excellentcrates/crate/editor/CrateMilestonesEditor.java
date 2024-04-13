package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.Comparator;
import java.util.stream.IntStream;

public class CrateMilestonesEditor extends EditorMenu<CratesPlugin, Crate> implements CrateEditor, AutoFilled<Milestone> {

    public CrateMilestonesEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_CRATES.getString(), 45);

        this.addReturn(39, (viewer, event, crate)-> {
            this.runNextTick(() -> this.plugin.getCrateManager().openCrateEditor(viewer.getPlayer(), crate));
        });
        this.addNextPage(44);
        this.addPreviousPage(36);

        this.addCreation(EditorLang.MILESTONE_CREATE, 41, (viewer, event, crate) -> {
            Milestone milestone = new Milestone(crate, "null", 0, 0);
            crate.getMilestones().add(milestone);
            this.saveMilestones(viewer, crate, true);
        });
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Milestone> autoFill) {
        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getObject(viewer).getMilestones().stream().sorted(Comparator.comparing(Milestone::getOpenings)).toList());
        autoFill.setItemCreator(milestone -> {
            Reward reward = milestone.getReward();
            ItemStack item = new ItemStack(reward == null ? ItemUtil.getSkinHead(Placeholders.SKIN_QUESTION_MARK) : reward.getPreview());
            ItemReplacer.create(item).readLocale(EditorLang.MILESTONE_OBJECT).hideFlags().trimmed()
                .replace(milestone.replacePlaceholders())
                .writeMeta();
            return item;
        });
        autoFill.setClickAction(milestone -> (viewer1, event) -> {
            this.editObject(viewer1, crate -> {
                if (event.isShiftClick()) {
                    if (event.isRightClick()) {
                        crate.getMilestones().remove(milestone);
                        this.saveMilestones(viewer, crate, true);
                        return;
                    }
                }

                if (event.isLeftClick()) {
                    this.handleInput(viewer1, Lang.EDITOR_ENTER_AMOUNT, (dialog, wrapper) -> {
                        milestone.setOpenings(wrapper.asInt());
                        this.saveMilestones(viewer, crate, false);
                        return true;
                    });
                }
                else if (event.isRightClick()) {
                    this.handleInput(viewer1, Lang.EDITOR_ENTER_REWARD_ID, (dialog, wrapper) -> {
                        milestone.setRewardId(wrapper.getTextRaw());
                        this.saveMilestones(viewer, crate, false);
                        return true;
                    }).setSuggestions(crate.getRewardsMap().keySet(), true);
                }
            });
        });
    }
}
