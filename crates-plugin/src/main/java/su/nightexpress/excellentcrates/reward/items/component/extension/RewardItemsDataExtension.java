package su.nightexpress.excellentcrates.reward.items.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.excellentcrates.reward.items.component.DefaultRewardItemsComponent;
import su.nightexpress.excellentcrates.reward.items.component.codec.RewardItemsComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardItemsDataExtension implements RewardDataExtension {

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.ITEMS, DefaultRewardItemsComponent.createDefault());
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
        RewardItemsComponent component = config.getOrSet(
            "items",
            RewardItemsComponentCodec.INSTANCE,
            DefaultRewardItemsComponent.createDefault()
        );
        builder.component(RewardComponentKeys.ITEMS, component);
    }

    @Override
    public void onUnload(Reward reward) {

    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        RewardItemsComponent component = reward.getComponentOrNull(RewardComponentKeys.ITEMS);

        config.set("items", component);
    }
}
