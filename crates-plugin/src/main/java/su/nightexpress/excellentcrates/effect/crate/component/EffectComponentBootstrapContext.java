package su.nightexpress.excellentcrates.effect.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.effect.crate.component.codec.EffectComponentCodec;
import su.nightexpress.excellentcrates.effect.crate.component.extension.EffectComponentDataExtension;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class EffectComponentBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("effect.crate.component");
    private static final String     NAME = "Component";

    private final EffectComponentDataExtension dataExtension;

    public EffectComponentBootstrapContext() {
        super(ID, NAME);

        ConfigCodecs.register(DefaultEffectComponent.class, EffectComponentCodec.INSTANCE);

        this.dataExtension = new EffectComponentDataExtension();
    }

    public EffectComponentDataExtension getDataExtension() {
        return this.dataExtension;
    }
}
