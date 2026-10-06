package su.nightexpress.excellentcrates.keys.cost.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.cost.component.model.DefaultKeyRequirementComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class KeyCostComponentExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.KEY_REQUIREMENT, new DefaultKeyRequirementComponent());
    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        DefaultKeyRequirementComponent component = config.getOrSet(
            "key_requirements",
            DefaultKeyRequirementComponent.class,
            DefaultKeyRequirementComponent.createDefault()
        );
        builder.component(CrateComponentKeys.KEY_REQUIREMENT, component);
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);

        config.set("key_requirements", component);
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
    public void onUnload(Crate crate) {

    }
}
