package su.nightexpress.excellentcrates.effect.model.spiral;

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
public class SpiralEffect implements EffectModel<SpiralEffectSettings> {

    private static final Identifier ID = new Identifier("spiral");

    private static final double RADIUS           = 1.0;
    private static final double VERTICAL_SPACING = 0.1;
    private static final double START_ANGLE      = 0.0;
    private static final double END_ANGLE        = 6 * Math.PI;
    private static final int    NUM_POINTS       = 50;

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Spiral (Default)",
        ParticleTypes.FLAME.builder()
            .amount(5)
            .build(),
        NUM_POINTS,
        1,
        10
    );

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<SpiralEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        SpiralEffectSettings settings = new SpiralEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, SpiralEffectSettings settings, int step) {
        double deltaAngle = (END_ANGLE - START_ANGLE) / NUM_POINTS;
        double angle = START_ANGLE + step * deltaAngle;
        double x = RADIUS * Math.cos(angle);
        double z = RADIUS * Math.sin(angle);
        double y = VERTICAL_SPACING * angle;
        Location location = origin.clone().add(x, y, z);

        effect.play(location);
    }
}
