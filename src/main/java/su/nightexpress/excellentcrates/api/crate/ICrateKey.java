package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ConfigHolder;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IEditable;
import su.nexmedia.engine.api.manager.IPlaceholder;

public interface ICrateKey extends ConfigHolder, IEditable, ICleanable, IPlaceholder {

    String PLACEHOLDER_ID = "%key_id%";
    String PLACEHOLDER_NAME = "%key_name%";
    String PLACEHOLDER_VIRTUAL = "%key_virtual%";
    String PLACEHOLDER_ITEM_NAME = "%key_item_name%";

    @NotNull String getId();

    @NotNull String getName();

    void setName(@NotNull String name);

    boolean isVirtual();

    void setVirtual(boolean isVirtual);

    @NotNull ItemStack getItem();

    void setItem(@NotNull ItemStack item);
}
