package su.nightexpress.excellentcrates.animation.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.animation.component.DefaultAnimationComponent;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class AnimationComponentDataExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.ANIMATION, DefaultAnimationComponent.createDefault());
    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        DefaultAnimationComponent openingComponent = config.getOrSet("animation",
            DefaultAnimationComponent.class,
            DefaultAnimationComponent.createDefault()
        );

        builder.component(CrateComponentKeys.ANIMATION, openingComponent);
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        AnimationComponent openingComponent = crate.getComponentOrNull(CrateComponentKeys.ANIMATION);
        config.set("animation", openingComponent);
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
