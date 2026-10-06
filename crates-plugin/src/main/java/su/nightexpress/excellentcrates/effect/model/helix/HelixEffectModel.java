package su.nightexpress.excellentcrates.effect.model.helix;

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
public class HelixEffectModel implements EffectModel<HelixEffectSettings> {

    private static final Identifier ID = new Identifier("helix");

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Helix (Default)",
        ParticleTypes.FLAME.create(),
        24,
        1,
        10
    );

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<HelixEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        HelixEffectSettings settings = new HelixEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, HelixEffectSettings settings, int step) {
        Location location = origin.add(0, 0.05D, 0);

        double x = Math.PI / 10 * step;
        double z = step * 0.1 % 2.5;
        double y = 0.75;

        Location left = EffectUtils.getPointOnCircle(location, true, x, y, z);
        Location right = EffectUtils.getPointOnCircle(location, true, x - Math.PI, y, z);

        effect.play(left);
        effect.play(right);
    }
}
