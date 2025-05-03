package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RewardListMenu extends LinkedMenu<CratesPlugin, Crate> implements Filled<Reward> {

    private static final String SKULL_SORT   = "5cce7359a25de6da56308e6a369c6372e2c30906c62647040da137a32addc9";
    private static final String SKULL_CREATE = "e3c81adc6c06d95c65b6c1089755a04d7ebc414f51ba66d14d0c4c1d71520df6";

    public RewardListMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_LIST.getString());

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));

        this.addItem(ItemUtil.getSkinHead(SKULL_CREATE), EditorLang.REWARD_CREATE, 42, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardCreation(viewer.getPlayer(), crate));
        });

        this.addItem(ItemUtil.getSkinHead(SKULL_SORT), EditorLang.REWARD_SORT, 38, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openRewardSort(viewer.getPlayer(), crate));
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
                .setHideComponents(true)
                .localized(EditorLang.REWARD_OBJECT)
                .replacement(replacer -> replacer.replace(reward.replaceAllPlaceholders()));
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
                crate.saveRewards();
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
}
