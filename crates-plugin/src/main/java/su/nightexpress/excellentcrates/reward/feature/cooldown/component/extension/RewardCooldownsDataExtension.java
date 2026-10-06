package su.nightexpress.excellentcrates.reward.feature.cooldown.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.reward.feature.cooldown.component.StandardRewardCooldownComponent;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownCachedDataService;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardCooldownsDataExtension implements RewardDataExtension {

    private final RewardCooldownCachedDataService cachedDataService;

    public RewardCooldownsDataExtension(RewardCooldownCachedDataService cachedDataService) {
        this.cachedDataService = cachedDataService;
    }

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.COOLDOWN, StandardRewardCooldownComponent.createDefault());
    }

    @Override
    public void onCreate(Reward reward) {

    }

    @Override
    public void onDelete(Reward reward) {
        this.cachedDataService.markAllRemovedByKey(reward.id());
    }

    @Override
    public void onLoad(Reward reward) {

    }

    @Override
    public void onRead(FileConfig config, RewardBuilder builder) {
        StandardRewardCooldownComponent cooldowns = config.getOrSet("cooldown",
            StandardRewardCooldownComponent.class,
            StandardRewardCooldownComponent.createDefault()
        );

        builder.component(RewardComponentKeys.COOLDOWN, cooldowns);
    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        config.set("cooldown", reward.getComponentOrNull(RewardComponentKeys.COOLDOWN));
    }
}
