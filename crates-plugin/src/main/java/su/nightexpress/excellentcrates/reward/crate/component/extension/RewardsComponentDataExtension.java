package su.nightexpress.excellentcrates.reward.crate.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardsComponentDataExtension implements RewardDataExtension {

    private final CrateRegistry crateRegistry;

    public RewardsComponentDataExtension(CrateRegistry crateRegistry) {
        this.crateRegistry = crateRegistry;
    }

    @Override
    public void onBuild(RewardBuilder builder) {

    }

    @Override
    public void onCreate(Reward reward) {

    }

    @Override
    public void onDelete(Reward reward) {
        // Remove the reward from all crates' rewards components
        this.crateRegistry.values().forEach(crate -> {
            CrateRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
            if (component == null) return;

            component.removeReward(reward.id());
        });
    }

    @Override
    public void onLoad(Reward reward) {

    }

    @Override
    public void onRead(FileConfig config, RewardBuilder builder) {

    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {

    }
}
