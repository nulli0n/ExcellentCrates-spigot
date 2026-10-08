package su.nightexpress.excellentcrates.keys.data.key;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public class StandardKeyItem implements KeyItem {

    private AdaptedItem item;
    private boolean     stackable;
    private boolean     inheritDisplaySettings;

    public StandardKeyItem(AdaptedItem item, boolean stackable, boolean inheritDisplaySettings) {
        this.item = item;
        this.stackable = stackable;
        this.inheritDisplaySettings = inheritDisplaySettings;
    }

    public static StandardKeyItem createDefault() {
        AdaptedItem item = ItemHelper.bukkit(new ItemStack(Material.TRIPWIRE_HOOK));

        return new StandardKeyItem(item, false, true);
    }

    @Override
    public AdaptedItem getItem() {
        return item;
    }

    @Override
    public void setItem(AdaptedItem item) {
        this.item = item;
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    @Override
    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    @Override
    public boolean isInheritDisplaySettings() {
        return inheritDisplaySettings;
    }

    @Override
    public void setInheritDisplaySettings(boolean inheritDisplaySettings) {
        this.inheritDisplaySettings = inheritDisplaySettings;
    }
}
