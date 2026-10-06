package su.nightexpress.excellentcrates.reward.selectable.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;
import su.nightexpress.excellentcrates.reward.selectable.component.codec.RewardSelectionComponentCodec;
import su.nightexpress.excellentcrates.reward.selectable.component.data.DefaultSelectiveRewardsComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SelectiveRewardsDataExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.SELECTABLE_REWARDS, DefaultSelectiveRewardsComponent.createDefault());
    }

    @Override
    public void onCreate(Crate reward) {

    }

    @Override
    public void onDelete(Crate reward) {

    }

    @Override
    public void onLoad(Crate reward) {

    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        SelectableRewardsComponent component = config.getOrSet(
            "selective_rewards",
            RewardSelectionComponentCodec.INSTANCE,
            DefaultSelectiveRewardsComponent.createDefault()
        );
        builder.component(CrateComponentKeys.SELECTABLE_REWARDS, component);
    }

    @Override
    public void onUnload(Crate reward) {

    }

    @Override
    public void onWrite(FileConfig config, Crate reward) {
        SelectableRewardsComponent component = reward.getComponentOrNull(CrateComponentKeys.SELECTABLE_REWARDS);
        config.set("selective_rewards", component);
    }
}
