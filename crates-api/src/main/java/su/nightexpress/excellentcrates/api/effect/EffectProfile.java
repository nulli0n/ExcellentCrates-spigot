package su.nightexpress.excellentcrates.api.effect;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyHolder;

@NullMarked
public record EffectProfile<T extends EffectModelSettings>(AdaptedKey key,
                                                           EffectModel<T> model,
                                                           EffectBaseSettings baseSettings,
                                                           T modelSettings) implements KeyHolder {

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }

    public void play(Location origin, ParticleEffect<?> effect, int step) {
        this.model.playStep(origin, effect, this.modelSettings, step);
    }
}
