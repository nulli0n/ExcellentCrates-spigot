package su.nightexpress.excellentcrates.reward.broadcast.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.reward.broadcast.component.DefaultRewardBroadcastComponent;
import su.nightexpress.excellentcrates.reward.broadcast.component.codec.RewardBroadcastComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardBroadcastDataExtension implements RewardDataExtension {

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.BROADCAST, DefaultRewardBroadcastComponent.createDefault());
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
        RewardBroadcastComponent component = config.getOrSet(
            "broadcast",
            RewardBroadcastComponentCodec.INSTANCE,
            DefaultRewardBroadcastComponent.createDefault()
        );
        builder.component(RewardComponentKeys.BROADCAST, component);
    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        RewardBroadcastComponent component = reward.getComponentOrNull(RewardComponentKeys.BROADCAST);
        config.set("broadcast", component);
    }
}
