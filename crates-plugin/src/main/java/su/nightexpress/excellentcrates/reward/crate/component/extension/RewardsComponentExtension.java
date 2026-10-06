package su.nightexpress.excellentcrates.reward.crate.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardsComponentCodec;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardsComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardsComponentExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.REWARDS, DefaultRewardsComponent.createDefault());
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {

    }

    @Override
    public void onLoad(Crate crate) {

    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        CrateRewardsComponent component = config.getOrSet("rewards",
            RewardsComponentCodec.INSTANCE,
            DefaultRewardsComponent.createDefault()
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
