package su.nightexpress.excellentcrates.api.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.Writeable;

public interface ItemProvider extends Writeable {

    boolean isDummy();

    boolean isValid();

    boolean canProduceItem();

    @NotNull String getItemType();

    @NotNull ItemStack getItemStack();

    @Nullable ItemStack createItemStack();

    @NotNull ItemType getType();
}
