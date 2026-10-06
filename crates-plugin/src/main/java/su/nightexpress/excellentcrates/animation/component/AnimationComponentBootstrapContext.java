package su.nightexpress.excellentcrates.animation.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.animation.component.codec.AnimationComponentCodec;
import su.nightexpress.excellentcrates.animation.component.extension.AnimationComponentDataExtension;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class AnimationComponentBootstrapContext {

    private final AnimationComponentDataExtension dataExtension;

    public AnimationComponentBootstrapContext() {
        ConfigCodecs.register(DefaultAnimationComponent.class, AnimationComponentCodec.INSTANCE);

        this.dataExtension = new AnimationComponentDataExtension();
    }

    public AnimationComponentDataExtension getDataExtension() {
        return this.dataExtension;
    }
}
