package su.nightexpress.excellentcrates.reward.preview;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.preview.NameAndLore;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardColorProvider;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class DefaultRewardPreviewAPI implements RewardPreviewAPI {

    private final RewardPreviewResolver previewResolver;
    private final RewardPreviewService  previewService;

    public DefaultRewardPreviewAPI(RewardPreviewResolver previewResolver, RewardPreviewService previewService) {
        this.previewResolver = previewResolver;
        this.previewService = previewService;
    }

    @Override
    public void setColorProvider(RewardColorProvider colorProvider) {
        this.previewService.setColorProvider(colorProvider);
    }

    @Override
    public NameAndLore getDisplayInfo(Reward reward) {
        return previewResolver.getDisplayInfo(reward);
    }

    @Override
    public NightItem createPreviewIconWithPlaceholders(Reward reward) {
        return previewService.createPreviewIconWithPlaceholders(reward);
    }

    @Override
    public NightItem createPreviewIcon(Reward reward) {
        return previewService.createPreviewIcon(reward);
    }

    @Override
    public ItemStack createDisplayItem(Reward reward) {
        return previewService.createDisplayItem(reward);
    }

    @Override
    public ItemStack createPreviewItem(Reward reward) {
        return previewService.createPreviewItem(reward);
    }

    @Override
    public Color getColor(Reward reward) {
        return previewService.getColor(reward);
    }
}
