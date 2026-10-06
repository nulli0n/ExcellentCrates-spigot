package su.nightexpress.excellentcrates.effect.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.excellentcrates.api.effect.EffectBaseSettings;

@NullMarked
public record DefaultEffectBaseSettings(String name,
                                        ParticleEffect<?> effect,
                                        int maxFrames,
                                        int tickInterval,
                                        int pauseTicks) implements EffectBaseSettings {

}
