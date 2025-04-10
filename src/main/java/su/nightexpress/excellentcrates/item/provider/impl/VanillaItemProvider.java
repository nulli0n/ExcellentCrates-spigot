package su.nightexpress.excellentcrates.item.provider.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.api.item.ItemType;
import su.nightexpress.excellentcrates.item.provider.AbstractItemProvider;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.text.NightMessage;

public class VanillaItemProvider extends AbstractItemProvider {

    private final ItemStack itemStack;

    public VanillaItemProvider(@NotNull ItemStack itemStack) {
        super(ItemType.VANILLA);
        this.itemStack = itemStack;
    }

    @NotNull
    public static VanillaItemProvider fromItem(@NotNull ItemStack itemStack) {
        return new VanillaItemProvider(itemStack);
    }

    @Nullable
    public static VanillaItemProvider read(@NotNull FileConfig config, @NotNull String path) {
        String itemTag = config.getString(path + ".ItemTag");
        if (itemTag != null) {
            ItemTag tag = new ItemTag(itemTag, Version.MC_1_21_4.getDataVersion());
            config.set(path + ".Tag", tag);
            config.remove(path + ".ItemTag");
        }

        ItemTag tag = ItemTag.read(config, path + ".Tag");

        ItemStack itemStack = ItemNbt.fromTag(tag);//.fromTagString(itemTag);
        if (itemStack == null) {
            //CratesAPI.error("Could not parse item tag string '" + itemTag + "'. Caused by '" + config.getFile().getName() + "' -> '" + path + "'.");
            return null;
        }

        return new VanillaItemProvider(itemStack);
    }

    @Override
    public void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Tag", ItemNbt.getTag(this.itemStack));
    }

    @Override
    public boolean isValid() {
        return !this.itemStack.getType().isAir();
    }

    @Override
    @Nullable
    public ItemStack createItemStack() {
        return new ItemStack(this.itemStack);
    }

    @Override
    @NotNull
    public String getItemType() {
        String display = StringUtil.transformForID(NightMessage.stripTags(ItemUtil.getSerializedName(this.itemStack)));
        if (!display.isBlank()) return display;

        return BukkitThing.toString(this.itemStack.getType());
    }
}
