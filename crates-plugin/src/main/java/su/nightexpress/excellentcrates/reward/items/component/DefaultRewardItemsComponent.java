package su.nightexpress.excellentcrates.reward.items.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public class DefaultRewardItemsComponent implements RewardItemsComponent {

    private List<AdaptedItem> items;

    public DefaultRewardItemsComponent(List<AdaptedItem> items) {
        this.items = new ArrayList<>(items);
    }

    public static DefaultRewardItemsComponent createDefault() {
        return new DefaultRewardItemsComponent(List.of());
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public List<AdaptedItem> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    @Override
    public void setItems(List<AdaptedItem> items) {
        this.items = new ArrayList<>(items);
    }

    @Override
    public void addItem(AdaptedItem item) {
        this.items.add(item);
    }

    @Override
    public void removeItem(AdaptedItem item) {
        this.items.remove(item);
    }

    @Override
    public @Nullable AdaptedItem removeItem(int index) {
        if (index >= 0 && index < this.items.size()) {
            return this.items.remove(index);
        }
        return null;
    }

    @Override
    public void clearItems() {
        this.items.clear();
    }
}
