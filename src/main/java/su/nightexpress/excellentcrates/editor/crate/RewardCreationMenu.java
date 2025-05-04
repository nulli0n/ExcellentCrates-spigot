package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.RewardFactory;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.stream.IntStream;

public class RewardCreationMenu extends LinkedMenu<CratesPlugin, RewardCreationMenu.Data> {

    private static final int ITEM_SLOT = 22;

    public record Data(@NotNull Crate crate, @Nullable ItemProvider provider){}

    public RewardCreationMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_CREATION.getString());

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).toMenuItem().setPriority(-1).setSlots(IntStream.range(0, 45).toArray()));

        this.addItem(NightItem.fromType(Material.LIGHT_BLUE_STAINED_GLASS_PANE).toMenuItem().setPriority(1).setSlots(10,11,12,19,21,28,29,30));

        this.addItem(NightItem.fromType(Material.MAGENTA_STAINED_GLASS_PANE).toMenuItem().setPriority(1).setSlots(14,15,16,23,25,32,33,34));

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardList(viewer.getPlayer(), this.getLink(viewer).crate));
        }));

        this.addItem(NightItem.fromType(Material.OAK_SIGN).localized(EditorLang.REWARD_CREATION_INFO).toMenuItem().setSlots(4));

        this.addItem(Material.COMMAND_BLOCK, EditorLang.REWARD_CREATION_COMMAND, 24, (viewer, event, obj) -> this.tryCreate(viewer, RewardType.COMMAND));
        this.addItem(Material.DIAMOND, EditorLang.REWARD_CREATION_ITEM, 20, (viewer, event, obj) -> this.tryCreate(viewer, RewardType.ITEM));
    }

    public void open(@NotNull Player player, @NotNull Crate crate, @Nullable ItemProvider provider) {
        this.open(player, new Data(crate, provider));
    }

    private void tryCreate(@NotNull MenuViewer viewer, @NotNull RewardType type) {
        Inventory inventory = viewer.getInventory();
        if (inventory == null) return;

        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        ItemProvider provider = data.provider;
        if (provider == null || !provider.canProduceItem()) return;

        ItemStack source = data.provider.getItemStack();
        Crate crate = data.crate;

        Reward reward = RewardFactory.wizardCreation(this.plugin, crate, source, type, data.provider);
        crate.addReward(reward);
        crate.saveRewards();

        this.runNextTick(() -> plugin.getEditorManager().openRewardList(player, crate));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
        Data data = this.getLink(viewer);
        ItemProvider provider = data.provider;
        ItemStack itemStack = null;

        if (provider != null) {
            itemStack = provider.getItemStack();
        }

        inventory.setItem(ITEM_SLOT, itemStack);
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        if (!result.isInventory()) return;

        ItemStack clicked = result.getItemStack();
        if (clicked == null || clicked.getType().isAir()) return;

        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        Crate crate = data.crate;

        if (!ItemTypes.isCustom(clicked)) {
            this.runNextTick(() -> this.open(player, crate, ItemTypes.vanilla(clicked)));
            return;
        }

        this.runNextTick(() -> this.plugin.getEditorManager().openItemTypeMenu(player, clicked,
            newProvider -> this.runNextTick(() -> this.open(player, crate, newProvider)))
        );
    }
}
