package su.nightexpress.excellentcrates.reward.feature.limit.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.reward.feature.limit.component.DefaultRewardLimitComponent;
import su.nightexpress.excellentcrates.reward.feature.limit.component.codec.RewardLimitComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardLimitsDataExtension implements RewardDataExtension {

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.LIMIT, DefaultRewardLimitComponent.createDefault());
    }

    @Override
    public void onCreate(Reward reward) {

    }

    @Override
    public void onDelete(Reward reward) {

    }

    @Override
    public void onLoad(Reward reward) {

    }

    @Override
    public void onRead(FileConfig config, RewardBuilder builder) {
        RewardLimitComponent limit = config.getOrSet("limit",
            RewardLimitComponentCodec.INSTANCE,
            DefaultRewardLimitComponent.createDefault()
        );

        builder.component(RewardComponentKeys.LIMIT, limit);
    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        config.set("limit", limit);
    }
}
