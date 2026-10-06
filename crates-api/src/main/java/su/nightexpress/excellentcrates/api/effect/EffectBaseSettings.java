package su.nightexpress.excellentcrates.api.effect;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;

@NullMarked
public interface EffectBaseSettings {

    String name();

    ParticleEffect<?> effect();

    int maxFrames();

    int tickInterval();

    int pauseTicks();
}
