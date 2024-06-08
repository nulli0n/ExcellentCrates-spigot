package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RewardListEditor extends EditorMenu<CratesPlugin, Crate> implements CrateEditor, AutoFilled<Reward> {

    private static final String TEXTURE_LETTERS = "5cce7359a25de6da56308e6a369c6372e2c30906c62647040da137a32addc9";

    public RewardListEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_CRATES.getString(), 54);

        this.addReturn(49, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getCrateManager().openCrateEditor(viewer.getPlayer(), crate));
        });
        this.addNextPage(53);
        this.addPreviousPage(45);

        this.addCreation(EditorLang.REWARD_CREATE, 51, (viewer, event, crate) -> {
            ItemStack cursor = event.getCursor();
            if (cursor != null && !cursor.getType().isAir()) {
                String id = StringUtil.lowerCaseUnderscore(ItemUtil.getItemName(cursor));
                int count = 0;
                while (crate.getReward(count == 0 ? id : id + count) != null) {
                    count++;
                }
                Reward reward = Reward.createEmpty(plugin, crate, count == 0 ? id : id + count);
                reward.setName(ItemUtil.getItemName(cursor));
                reward.getItems().add(new ItemStack(cursor));
                reward.setPreview(cursor);
                crate.addReward(reward);
                event.getView().setCursor(null);
                this.saveRewards(viewer, crate, true);
                return;
            }

            this.handleInput(viewer, Lang.EDITOR_ENTER_REWARD_ID, (dialog, wrapper) -> {
                String id = StringUtil.lowerCaseUnderscore(wrapper.getTextRaw());
                if (crate.getReward(id) != null) {
                    dialog.error(Lang.ERROR_DUPLICATED_REWARD.getMessage());
                    return false;
                }
                Reward reward = Reward.createEmpty(plugin, crate, id);
                crate.addReward(reward);
                this.saveRewards(viewer, crate, false);
                return true;
            });
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_LETTERS), EditorLang.REWARD_SORT, 47, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getCrateManager().openRewardSortEditor(viewer.getPlayer(), crate));
        });
    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Reward> autoFill) {
        /*PreviewMenu previewMenu = this.plugin.getCrateManager().getPreview(this.getObject(viewer));
        if (previewMenu != null) {
            previewMenu.getItems().forEach(menuItem -> {
                MenuItem clone = menuItem.copy().setPriority(-1).setOptions(ItemOptions.personalWeak(viewer.getPlayer()));
                this.addItem(clone);
            });

            autoFill.setSlots(previewMenu.getRewardSlots());
        }
        else {*/
            autoFill.setSlots(IntStream.range(0, 36).toArray());
        //}

        autoFill.setItems(this.getObject(viewer).getRewards());
        autoFill.setItemCreator(reward -> {
            ItemStack item = new ItemStack(reward.getPreview());
            ItemReplacer.create(item).readLocale(EditorLang.REWARD_OBJECT).hideFlags().trimmed()
                .replace(reward.getAllPlaceholders())
                .writeMeta();
            return item;
        });

        autoFill.setClickAction(reward -> (viewer1, event) -> {
            this.editObject(viewer1, crate -> {
                if (event.getClick() == ClickType.DROP) {
                    crate.removeReward(reward);
                    this.saveRewards(viewer1, crate, true);
                    return;
                }

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
                    this.saveRewards(viewer1, crate, true);
                    return;
                }

                if (event.isLeftClick()) {
                    this.runNextTick(() -> plugin.getCrateManager().openRewardEditor(viewer1.getPlayer(), reward));
                }
            });
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
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }
}
