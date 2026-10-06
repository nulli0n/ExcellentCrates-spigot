package su.nightexpress.excellentcrates.effect.crate.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.excellentcrates.effect.crate.component.DefaultEffectComponent;
import su.nightexpress.excellentcrates.effect.crate.component.codec.EffectComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class EffectComponentDataExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.EFFECT, DefaultEffectComponent.createDefault());
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
        EffectComponent component = config.getOrSet("effect",
            EffectComponentCodec.INSTANCE,
            DefaultEffectComponent.createDefault()
        );

        builder.component(CrateComponentKeys.EFFECT, component);
    }

    @Override
    public void onUnload(Crate crate) {
        // We do not need to do anything special when unloading the effect component,
        // because it relies on the CratePositionObserver, which handles the necessary cleanup automatically.
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        EffectComponent effectComponent = crate.getComponentOrNull(CrateComponentKeys.EFFECT);
        config.set("effect", effectComponent);
    }
}
