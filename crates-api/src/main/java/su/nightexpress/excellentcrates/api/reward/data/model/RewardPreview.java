package su.nightexpress.excellentcrates.api.reward.data.model;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public interface RewardPreview {

    String getName();

    void setName(String name);

    List<String> getLore();

    void setLore(List<String> lore);

    AdaptedItem getIcon();

    void setIcon(AdaptedItem icon);

    boolean isInheritFromIcon();

    void setInheritFromIcon(boolean inheritFromIcon);
}
