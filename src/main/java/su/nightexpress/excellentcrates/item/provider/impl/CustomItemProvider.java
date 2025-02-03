package su.nightexpress.excellentcrates.item.provider.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.economybridge.ItemBridge;
import su.nightexpress.economybridge.api.item.ItemHandler;
import su.nightexpress.excellentcrates.api.item.ItemType;
import su.nightexpress.excellentcrates.item.provider.AbstractItemProvider;
import su.nightexpress.nightcore.config.FileConfig;

public class CustomItemProvider extends AbstractItemProvider {

    private final String handlerName;
    private final String itemId;
    private final int amount;

    public CustomItemProvider(@NotNull String handlerName, @NotNull String itemId, int amount) {
        super(ItemType.CUSTOM);
        this.handlerName = handlerName;
        this.itemId = itemId;
        this.amount = Math.max(1, amount);
    }

    @Nullable
    public static CustomItemProvider fromItem(@NotNull ItemStack itemStack) {
        ItemHandler handler = ItemBridge.getHandler(itemStack);
        if (handler == null) return null;

        String itemId = handler.getItemId(itemStack);
        if (itemId == null) return null;

        return new CustomItemProvider(handler.getName(), itemId, itemStack.getAmount());
    }

    @NotNull
    public static CustomItemProvider read(@NotNull FileConfig config, @NotNull String path) {
        String handlerName = config.getString(path + ".Handler", "null");
        String itemId = config.getString(path + ".ItemId", "null");
        int amount = config.getInt(path + ".Amount");

        // May cause errors if item's plugin was not yet loaded.
//        ItemHandler handler = ItemBridge.getHandler(handlerName);
//        if (handler == null) {
//            CratesAPI.error("Invalid custom item handler '" + handlerName + "'. Caused by '" + config.getFile().getName() + "' -> '" + path + "'.");
//        }
//        else if (!handler.isValidId(itemId)) {
//            CratesAPI.error("Invalid custom item id '" + itemId + "'. Caused by '" + config.getFile().getName() + "' -> '" + path + "'.");
//        }

        return new CustomItemProvider(handlerName, itemId, amount);
    }

    @Override
    public void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Handler", this.handlerName);
        config.set(path + ".ItemId", this.itemId);
        config.set(path + ".Amount", this.amount);
    }

    @Override
    @Nullable
    public ItemStack createItemStack() {
        ItemStack itemStack = ItemBridge.createItem(this.handlerName, this.itemId);
        if (itemStack != null) {
            itemStack.setAmount(this.amount);
        }
        return itemStack;
    }

    @Override
    @NotNull
    public String getItemType() {
        return this.getItemId();
    }

    @Override
    public boolean isValid() {
        ItemHandler handler = ItemBridge.getHandler(this.handlerName);
        if (handler == null) return false;

        return handler.isValidId(this.itemId);
    }

    @NotNull
    public String getHandlerName() {
        return this.handlerName;
    }

    @NotNull
    public String getItemId() {
        return this.itemId;
    }

    public int getAmount() {
        return this.amount;
    }
}
