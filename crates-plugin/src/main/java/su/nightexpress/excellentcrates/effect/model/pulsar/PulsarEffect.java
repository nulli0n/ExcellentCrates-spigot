package su.nightexpress.excellentcrates.effect.model.pulsar;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.bukkit.particle.ParticleTypes;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.effect.EffectModel;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.excellentcrates.effect.data.codec.EffectBaseSettingsCodec;
import su.nightexpress.excellentcrates.effect.data.model.DefaultEffectBaseSettings;
import su.nightexpress.excellentcrates.effect.util.EffectUtils;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class PulsarEffect implements EffectModel<PulsarEffectSettings> {

    private static final Identifier ID = new Identifier("pulsar");

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Pulsar (Default)",
        ParticleTypes.FLAME.builder()
            .amount(2)
            .xOffset(0.1)
            .yOffset(0.1)
            .zOffset(0.1)
            .build(),
        38,
        2,
        10
    );

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<PulsarEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        PulsarEffectSettings settings = new PulsarEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, PulsarEffectSettings settings, int step) {
        Location shifted = origin.clone().add(0, -0.8D, 0);
        double y = (0.5 + step * 0.15) % 3.0;
        for (int point = 0; point < y * 10.0; ++point) {
            double x = 2 * Math.PI / (y * 10.0) * point;
            Location location = EffectUtils.getPointOnCircle(shifted.clone(), false, x, y, 1.0);
            effect.play(location);
        }
    }
}
