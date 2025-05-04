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
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;
import java.util.stream.IntStream;

public class RewardContentMenu extends LinkedMenu<CratesPlugin, ItemReward> {

    public RewardContentMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_CONTENT.getString());

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(0, 9).toArray()));
        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(36, 45).toArray()));

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardOptions(viewer.getPlayer(), this.getLink(viewer)));
        }));
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        ItemStack clicked = result.getItemStack();
        if (clicked == null || clicked.getType().isAir()) return;

        Player player = viewer.getPlayer();
        ItemStack copy = new ItemStack(clicked);
        ItemReward reward = this.getLink(viewer);

        if (result.isInventory()) {
            if (!ItemTypes.isCustom(copy)) {
                this.addItem(reward, ItemTypes.vanilla(copy));
                this.runNextTick(() -> this.flush(viewer));
            }
            else {
                this.runNextTick(() -> plugin.getEditorManager().openItemTypeMenu(player, copy, provider -> {
                    this.addItem(reward, provider);
                    this.runNextTick(() -> this.open(player, reward));
                }));
            }
        }
        else {
            int slot = result.getSlot();
            if (slot < 9 || slot > CrateUtils.REWARD_ITEMS_LIMIT) return;

            Players.addItem(player, copy);

            if (event.isRightClick()) return;

            int index = slot - 9;
            reward.getItems().remove(index);
            reward.getCrate().saveReward(reward);
            this.runNextTick(() -> this.flush(viewer));
        }

        clicked.setAmount(0);
    }

    private void addItem(@NotNull ItemReward reward, @NotNull ItemProvider provider) {
        reward.addItem(provider);
        reward.getCrate().saveReward(reward);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    public void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
        List<ItemProvider> items = this.getLink(viewer).getItems();

        for (int index = 0; index < CrateUtils.REWARD_ITEMS_LIMIT; index++) {
            if (index >= items.size()) break;

            int slot = index + 9;
            inventory.setItem(slot, items.get(index).getItemStack());
        }
    }
}
