package su.nightexpress.excellentcrates.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.dummy.DummyItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public final class ItemHelper {

    private ItemHelper() {
    }

    public static ItemStack createPlaceholder() {
        return NightItem.fromType(Material.BARRIER)
            .hideAllComponents()
            .localized(Lang.UI_ITEM_PLACEHOLDER)
            .getItemStack();
    }

    public static ItemStack toItemStack(AdaptedItem item) {
        return item.itemStack().orElse(createPlaceholder());
    }

    public static boolean isBroken(AdaptedItem item) {
        return item instanceof DummyItem;
    }

    public static boolean isCustom(ItemStack itemStack) {
        return !isBukkitOnly(itemStack);
    }

    public static boolean isMixed(ItemStack itemStack) {
        return ItemBridge.get().isMixed(itemStack);
    }

    public static boolean isBukkitOnly(ItemStack itemStack) {
        return ItemBridge.get().isBukkitOnly(itemStack);
    }

    public static AdaptedItem bukkit(ItemStack itemStack) {
        return ItemBridge.get().getBukkitProvider().wrapItem(itemStack).orElse(DummyItem.INSTANCE);
    }

    public static AdaptedItem bukkitIfMixed(ItemStack itemStack) {
        if (isMixed(itemStack)) {
            return bukkitIfCrates(itemStack);
        }

        return adapt(itemStack);
    }

    public static AdaptedItem bukkitIfCrates(ItemStack itemStack) {
        if (ItemBridge.get().isProducedBy(itemStack, ItemProviderKeys.EXCELLENT_CRATES)) {
            return bukkit(itemStack);
        }

        return adapt(itemStack);
    }

    public static AdaptedItem adapt(ItemStack itemStack) {
        return ItemBridge.get().adaptOrDummy(itemStack);
    }
}
