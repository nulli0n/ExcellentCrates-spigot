package su.nightexpress.excellentcrates.editor.generic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ItemTypeMenu extends LinkedMenu<CratesPlugin, ItemTypeMenu.Data> {

    public static final int ITEM_SLOT = 13;

    public record Data(@NotNull Consumer<ItemProvider> result, @NotNull ItemStack itemStack) {}

    public ItemTypeMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X3, Lang.EDITOR_TITLE_ITEM_TYPE.getString());

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).toMenuItem().setPriority(-1).setSlots(IntStream.range(0, 27).toArray()));

        this.addItem(NightItem.asCustomHead("a71402201d4d8f4613df7ca4c691625f1dc5ff429a90cf1f51668d80619758a3")
            .localized(Lang.EDITOR_BUTTON_ITEM_TYPE_BY_NBT)
            .toMenuItem()
            .setHandler(ItemHandler.forClick((viewer, event) -> this.select(viewer, ItemTypes::vanilla)))
            .setSlots(10));

        this.addItem(NightItem.asCustomHead("543f5ed19f634370dd9c5b9e8772c06f9917a3cf1c69b4433a5243b058d2d44c")
            .localized(Lang.EDITOR_BUTTON_ITEM_TYPE_BY_ID)
            .toMenuItem()
            .setHandler(ItemHandler.forClick((viewer, event) -> this.select(viewer, ItemTypes::fromItem)))
            .setSlots(16));
    }

    public void open(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull Consumer<ItemProvider> result) {
        this.open(player, new Data(result, itemStack));
    }

    private void select(@NotNull MenuViewer viewer, @NotNull Function<ItemStack, ItemProvider> function) {
        Inventory inventory = viewer.getInventory();
        if (inventory == null) return;

        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        ItemStack itemStack = data.itemStack;

        ItemProvider provider = function.apply(itemStack);
        data.result.accept(provider);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
        ItemStack itemStack = this.getLink(viewer).itemStack;
        inventory.setItem(ITEM_SLOT, itemStack);
    }
}
