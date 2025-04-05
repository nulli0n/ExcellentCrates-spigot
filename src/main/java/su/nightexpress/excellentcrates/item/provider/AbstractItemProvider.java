package su.nightexpress.excellentcrates.item.provider;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.api.item.ItemType;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public abstract class AbstractItemProvider implements ItemProvider {

    private final ItemType type;

    public AbstractItemProvider(@NotNull ItemType type) {
        this.type = type;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        if (this.isDummy()) return;

        config.set(path + ".Type", this.type.name());
        this.writeAdditional(config, path);
    }

    protected abstract void writeAdditional(@NotNull FileConfig config, @NotNull String path);

    @NotNull
    protected ItemStack getDummyItem() {
        ItemStack itemStack = NightItem.fromType(Material.BARRIER).localized(Lang.OTHER_BROKEN_ITEM).getItemStack();
        PDCUtil.set(itemStack, Keys.dummyItem, true);
        return itemStack;
    }

    public boolean isDummy() {
        return false;
    }

    @Override
    public boolean canProduceItem() {
        return !this.isDummy() && this.isValid();
    }

    @NotNull
    public ItemStack getItemStack() {
        ItemStack itemStack = this.createItemStack();
        if (itemStack == null || itemStack.getType().isAir()) {
            return this.getDummyItem();
        }
        return itemStack;
    }

    @NotNull
    public ItemType getType() {
        return this.type;
    }
}
