package su.nightexpress.excellentcrates.api.effect;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface EffectModel<T extends EffectModelSettings> extends Identifiable {

    /**
     * @param origin   The center location of the crate block
     * @param settings The effect settings
     * @param step     The current animation frame
     */
    void playStep(Location origin, ParticleEffect<?> effect, @NonNull T settings, int step);

    EffectProfile<T> loadProfile(AdaptedKey key, FileConfig config);

    void writeDefaultProfile(FileConfig config);
}
