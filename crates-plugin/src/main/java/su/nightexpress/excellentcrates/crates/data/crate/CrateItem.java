package su.nightexpress.excellentcrates.crates.data.crate;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public class CrateItem implements ICrateItem {

    private AdaptedItem item;
    private boolean     itemStackable;
    private boolean     useDisplay;

    public CrateItem(AdaptedItem item, boolean itemStackable, boolean useDisplay) {
        this.item = item;
        this.itemStackable = itemStackable;
        this.useDisplay = useDisplay;
    }

    public static CrateItem createDefault() {
        AdaptedItem item = ItemHelper.bukkit(new ItemStack(Material.CHEST));
        boolean stackable = true;
        boolean useDisplay = true;

        return new CrateItem(item, stackable, useDisplay);
    }

    @Override
    public AdaptedItem getItem() {
        return this.item;
    }

    @Override
    public void setItem(AdaptedItem item) {
        this.item = item;
    }

    @Override
    public boolean isStackable() {
        return this.itemStackable;
    }

    @Override
    public void setStackable(boolean itemStackable) {
        this.itemStackable = itemStackable;
    }

    @Override
    public boolean isUseDisplay() {
        return this.useDisplay;
    }

    @Override
    public void setUseDisplay(boolean useDisplay) {
        this.useDisplay = useDisplay;
    }
}
