package su.nightexpress.excellentcrates.effect.model.beacon;

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
public class BeaconEffect implements EffectModel<BeaconEffectSettings> {

    private static final Identifier ID = new Identifier("beacon");

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Beacon (Default)",
        ParticleTypes.FLAME.builder()
            .amount(4)
            .yOffset(0.15)
            .build(),
        40,
        3,
        10
    );

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<BeaconEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        BeaconEffectSettings settings = new BeaconEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, BeaconEffectSettings settings, int step) {
        double x = 2 * Math.PI / 7D * step;
        for (int yStep = step; yStep > Math.max(0, step - 25); --yStep) {
            Location location = EffectUtils.getPointOnCircle(origin, true, x, 0.55, yStep * 0.75);

            effect.play(location);
        }
    }
}
