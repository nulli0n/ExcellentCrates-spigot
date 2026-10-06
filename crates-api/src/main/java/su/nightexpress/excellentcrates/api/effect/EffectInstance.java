package su.nightexpress.excellentcrates.api.effect;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EffectInstance {

    void tick(EffectRegistry registry);
}
