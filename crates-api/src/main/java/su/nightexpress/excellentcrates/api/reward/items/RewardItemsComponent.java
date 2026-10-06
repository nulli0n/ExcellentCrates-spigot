package su.nightexpress.excellentcrates.api.reward.items;

import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public interface RewardItemsComponent extends RewardComponent {

    boolean isEmpty();

    List<AdaptedItem> getItems();

    void setItems(List<AdaptedItem> items);

    void addItem(AdaptedItem item);

    void removeItem(AdaptedItem item);

    @Nullable
    AdaptedItem removeItem(int index);

    void clearItems();
}
