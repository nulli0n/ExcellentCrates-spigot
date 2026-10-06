package su.nightexpress.excellentcrates.reward.feature.commands.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.reward.feature.commands.component.codec.RewardCommandContentCodec;
import su.nightexpress.excellentcrates.reward.feature.commands.component.data.DefaultRewardCommandsComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardCommandsDataExtension implements RewardDataExtension {

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.COMMANDS, DefaultRewardCommandsComponent.createDefault());
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
        DefaultRewardCommandsComponent content = config.getOrSet("commands",
            RewardCommandContentCodec.INSTANCE,
            DefaultRewardCommandsComponent.createDefault()
        );

        builder.component(RewardComponentKeys.COMMANDS, content);
    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        config.set("commands", reward.getComponentOrNull(RewardComponentKeys.COMMANDS));
    }
}
