package su.nightexpress.excellentcrates.api.reward.preview;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.preview.NameAndLore;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public interface RewardPreviewAPI {

    void setColorProvider(RewardColorProvider colorProvider);

    NightItem createPreviewIconWithPlaceholders(Reward reward);

    NightItem createPreviewIcon(Reward reward);

    ItemStack createDisplayItem(Reward reward);

    ItemStack createPreviewItem(Reward reward);

    NameAndLore getDisplayInfo(Reward reward);

    Color getColor(Reward reward);
}
