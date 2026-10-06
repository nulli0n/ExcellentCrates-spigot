package su.nightexpress.excellentcrates.effect.model.vortex;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.bukkit.particle.ParticleTypes;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.effect.EffectModel;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.excellentcrates.effect.data.codec.EffectBaseSettingsCodec;
import su.nightexpress.excellentcrates.effect.data.model.DefaultEffectBaseSettings;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class VortexEffect implements EffectModel<VortexEffectSettings> {

    private static final Identifier ID = new Identifier("vortex");

    private static final int    STRANDS   = 2;
    private static final int    PARTICLES = 170 / 5;
    private static final float  RADIUS    = 1.5F;
    private static final float  CURVE     = 2.0F;
    private static final double ROTATION  = 45 * (Math.PI / 180);

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Vortex (Default)",
        ParticleTypes.FLAME.builder()
            .amount(1)
            .xOffset(0.1)
            .yOffset(0.1)
            .zOffset(0.1)
            .build(),
        PARTICLES,
        1,
        10
    );

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<VortexEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        VortexEffectSettings settings = new VortexEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, VortexEffectSettings settings, int step) {
        for (int boost = 0; boost < 3; boost++) {
            for (int strand = 1; strand <= STRANDS; ++strand) {
                float progress = step / (float) PARTICLES;
                double point = CURVE * progress * 2.0f * Math.PI / STRANDS + 2 * Math.PI * strand / STRANDS + ROTATION;
                double addX = Math.cos(point) * progress * RADIUS;
                double addZ = Math.sin(point) * progress * RADIUS;
                double addY = 3.5D - 0.02 * 5 * step;
                Location location = origin.clone().add(addX, addY, addZ);
                effect.play(location);
            }
        }
    }
}
