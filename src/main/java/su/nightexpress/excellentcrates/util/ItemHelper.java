package su.nightexpress.excellentcrates.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.impl.AdaptedCustomStack;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;
import su.nightexpress.nightcore.integration.item.impl.AdaptedVanillaStack;
import su.nightexpress.nightcore.util.ItemTag;
import su.nightexpress.nightcore.util.Version;

import java.util.Optional;

public class ItemHelper {

    @NotNull
    public static AdaptedItem readOrPlaceholder(@NotNull FileConfig config, @NotNull String path) {
        return read(config, path).orElse(vanilla(CrateUtils.getQuestionStack()));
    }

    @NotNull
    public static Optional<AdaptedItem> read(@NotNull FileConfig config, @NotNull String path) {
        String oldType = config.getString(path + ".Type");
        if (oldType != null) {
            AdaptedItem adaptedItem = null;

            if (oldType.equalsIgnoreCase("vanilla")) {
                String itemTag = config.getString(path + ".ItemTag");
                if (itemTag != null) {
                    ItemTag tag = new ItemTag(itemTag, Version.MC_1_21.getDataVersion());
                    config.set(path + ".Tag", tag);
                    config.remove(path + ".ItemTag");
                }

                if (config.contains(path + ".Tag")) {
                    ItemTag tag = ItemTag.read(config, path + ".Tag");
                    adaptedItem = new AdaptedVanillaStack(tag);
                    config.remove(path + ".Tag");
                }
            }
            else if (oldType.equalsIgnoreCase("custom")) {
                String handlerName = config.getString(path + ".Handler", "null");
                String itemId = config.getString(path + ".ItemId", "null");
                int amount = config.getInt(path + ".Amount");

                ItemAdapter<?> adapter = ItemBridge.getAdapter(handlerName);
                if (adapter instanceof IdentifiableItemAdapter identifiableItemAdapter) {
                    adaptedItem = new AdaptedCustomStack(identifiableItemAdapter, new ItemIdData(itemId, amount));
                }

                config.remove(path + ".Handler");
                config.remove(path + ".ItemId");
                config.remove(path + ".Amount");
            }

            config.remove(path + ".Type");

            if (adaptedItem != null) {
                config.set(path, adaptedItem);
            }
        }

        return Optional.ofNullable(AdaptedItemStack.read(config, path));
    }

    @NotNull
    public static ItemStack toItemStack(@NotNull AdaptedItem item) {
        return item.itemStack().orElse(CrateUtils.getQuestionStack());
    }

    public static boolean isCustom(@NotNull ItemStack itemStack) {
        ItemAdapter<?> adapter = ItemBridge.getAdapter(itemStack);
        return adapter != null && !adapter.isVanilla();
    }

    @NotNull
    public static AdaptedItem vanilla(@NotNull ItemStack itemStack) {
        return AdaptedVanillaStack.of(itemStack);
    }

    @NotNull
    public static AdaptedItem adapt(@NotNull ItemStack itemStack) {
        ItemAdapter<?> adapter = ItemBridge.getAdapterOrVanilla(itemStack);
        AdaptedItem item = adapter.adapt(itemStack).orElse(null);
        return item == null ? vanilla(itemStack) : item;
    }

    @NotNull
    public static AdaptedItem adapt(@NotNull ItemStack itemStack, boolean allowCustoms) {
        return allowCustoms ? adapt(itemStack) : vanilla(itemStack);
    }
}
