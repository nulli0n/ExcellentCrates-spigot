package su.nightexpress.excellentcrates.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.impl.AdaptedVanillaStack;
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

    public static AdaptedItem adaptedPlaceholder() {
        return BrokenAdaptedItem.INSTANCE;
        //return AdaptedVanillaStack.of(createPlaceholder());
    }

    public static boolean isBroken(AdaptedItem item) {
        return item instanceof BrokenAdaptedItem;
    }

    public static ItemStack toItemStack(AdaptedItem item) {
        return item.itemStack().orElse(createPlaceholder());
    }

    public static boolean isCustom(ItemStack itemStack) {
        ItemAdapter<?> adapter = ItemBridge.getAdapter(itemStack);
        return adapter != null && !adapter.isVanilla();
    }

    public static AdaptedItem vanilla(ItemStack itemStack) {
        return AdaptedVanillaStack.of(itemStack);
    }

    public static AdaptedItem vanillaIfMixed(ItemStack itemStack) {
        if (isMixedItem(itemStack)) {
            return vanilla(itemStack);
        }

        return adapt(itemStack);
    }

    public static AdaptedItem adapt(ItemStack itemStack) {
        ItemAdapter<?> adapter = ItemBridge.getAdapterOrVanilla(itemStack);
        AdaptedItem item = adapter.adapt(itemStack).orElse(null);
        return item == null ? vanilla(itemStack) : item;
    }

    public static AdaptedItem adapt(ItemStack itemStack, boolean allowCustoms) {
        return allowCustoms ? adapt(itemStack) : vanilla(itemStack);
    }

    public static boolean isMixedItem(ItemStack itemStack) {
        return ItemBridge.getAdapters()
            .stream()
            .filter(handler -> handler.canHandle(itemStack) && !handler.isVanilla())
            .count() > 1;
    }

    public static boolean isVanillaOnly(ItemStack itemStack) {
        return ItemBridge.getAdapterOrVanilla(itemStack).isVanilla();
    }
}
