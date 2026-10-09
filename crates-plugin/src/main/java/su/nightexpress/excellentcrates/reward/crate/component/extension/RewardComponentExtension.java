package su.nightexpress.excellentcrates.reward.crate.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardsComponentCodec;
import su.nightexpress.excellentcrates.reward.crate.component.model.StandardRewardsComponent;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardComponentExtension implements CrateDataExtension {

    private final RewardDataService dataService;

    public RewardComponentExtension(RewardDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.REWARDS, StandardRewardsComponent.createDefault());
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {
        // Delete all rewards associated with this crate when it is deleted.

        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) return;

        Identifier crateId = crate.id();

        crateRewards.getRewardByIdMap().keySet().forEach(rewardId -> {
            Reward reward = this.dataService.getReward(new RewardId(crateId, rewardId));
            if (reward == null) return;

            this.dataService.deleteReward(reward);
        });
    }

    @Override
    public void onLoad(Crate crate) {

    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        CrateRewardsComponent component = config.getOrSet("rewards",
            RewardsComponentCodec.INSTANCE,
            StandardRewardsComponent.createDefault()
        );
        builder.component(CrateComponentKeys.REWARDS, component);
    }

    @Override
    public void onUnload(Crate crate) {

    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        CrateRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        config.set("rewards", component);
    }
}
