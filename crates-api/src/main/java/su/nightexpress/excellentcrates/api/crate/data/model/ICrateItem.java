package su.nightexpress.excellentcrates.api.crate.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public interface ICrateItem {

    AdaptedItem getItem();

    void setItem(AdaptedItem item);

    boolean isStackable();

    void setStackable(boolean stackable);

    boolean isUseDisplay();

    void setUseDisplay(boolean useDisplay);
}
