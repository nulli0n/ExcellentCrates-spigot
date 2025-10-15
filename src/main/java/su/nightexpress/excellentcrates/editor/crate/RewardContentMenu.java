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
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.util.CrateUtils;
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
import java.util.stream.IntStream;

import static su.nightexpress.excellentcrates.Placeholders.GENERIC_STATE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.DARK_GRAY;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_YELLOW;

public class RewardContentMenu extends LinkedMenu<CratesPlugin, ItemReward> implements LangContainer {

    private static final IconLocale LOCALE_PLACEHOLDERS = LangEntry.iconBuilder("Editor.Button.Reward.ItemPlaceholders")
        .name("Item Placeholders")
        .appendCurrent("State", GENERIC_STATE).br()
        .appendInfo("Allows to replace item placeholders:")
        .appendInfo(SOFT_YELLOW.wrap("→") + " Crate placeholders " + DARK_GRAY.wrap("(see docs)"))
        .appendInfo(SOFT_YELLOW.wrap("→") + " Reward placeholders " + DARK_GRAY.wrap("(see docs)"))
        .appendInfo(SOFT_YELLOW.wrap("→") + " " + Placeholders.PLAYER_NAME + " placeholder.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public RewardContentMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_CONTENT.text());
        this.plugin.injectLang(this);

        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 9).toArray()));
        this.addItem(MenuItem.background(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray()));

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
        Crate crate = reward.getCrate();
        Runnable flush = () -> this.flush(player);

        if (result.isInventory()) {
            if (!ItemHelper.isCustom(copy)) {
                reward.addItem(ItemHelper.vanilla(copy));
                crate.markDirty();
                this.runNextTick(flush);
            }
            else {
                CrateDialogs.REWARD_ITEM.ifPresent(dialog -> dialog.show(player, reward, copy, flush));
            }
        }
        else {
            int slot = result.getSlot();
            if (slot < 9 || slot > CrateUtils.REWARD_ITEMS_LIMIT) return;

            Players.addItem(player, copy);

            if (event.isRightClick()) return;

            int index = slot - 9;
            reward.getItems().remove(index);
            crate.markDirty();
            clicked.setAmount(0);
            this.runNextTick(flush);
        }
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        Player player = viewer.getPlayer();
        ItemReward reward = this.getLink(player);

        viewer.addItem(NightItem.fromType(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).localized(LOCALE_PLACEHOLDERS)
            .replacement(replacer -> replacer.replace(GENERIC_STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(reward.isAllowItemPlaceholders())))
            .toMenuItem().setSlots(4).setHandler((viewer1, event) -> {
                reward.setAllowItemPlaceholders(!reward.isAllowItemPlaceholders());
                reward.getCrate().markDirty();
                this.runNextTick(() -> this.flush(player));
            }).build()
        );
    }

    @Override
    public void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
        List<AdaptedItem> items = this.getLink(viewer).getItems();

        for (int index = 0; index < CrateUtils.REWARD_ITEMS_LIMIT; index++) {
            if (index >= items.size()) break;

            int slot = index + 9;
            inventory.setItem(slot, items.get(index).getItemStack());
        }
    }
}
