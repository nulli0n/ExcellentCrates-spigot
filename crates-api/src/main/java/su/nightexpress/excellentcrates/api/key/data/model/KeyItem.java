package su.nightexpress.excellentcrates.api.key.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public interface KeyItem {

    AdaptedItem getItem();

    void setItem(AdaptedItem item);

    boolean isStackable();

    void setStackable(boolean stackable);

    boolean isInheritDisplaySettings();

    void setInheritDisplaySettings(boolean inheritDisplaySettings);
}
