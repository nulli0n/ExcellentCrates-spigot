package su.nightexpress.excellentcrates.effect.model.sphere;

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
public class SphereEffect implements EffectModel<SphereEffectSettings> {

    private static final Identifier ID = new Identifier("sphere");

    private static final double DELTA_ANGLE = Math.PI / 10.0;
    private static final int    NUM_CIRCLES = 8;
    private static final int    NUM_POINTS  = 10;

    private static final DefaultEffectBaseSettings DEFAULT_SETTINGS = new DefaultEffectBaseSettings(
        "Sphere (Default)",
        ParticleTypes.FLAME.create(),
        NUM_CIRCLES,
        1,
        10
    );

    private record Point3D(double x, double y, double z) {

    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public EffectProfile<SphereEffectSettings> loadProfile(AdaptedKey key, FileConfig config) {
        DefaultEffectBaseSettings baseSettings = config.getOrSet("base-settings",
            EffectBaseSettingsCodec.INSTANCE,
            DEFAULT_SETTINGS
        );

        SphereEffectSettings settings = new SphereEffectSettings();

        return new EffectProfile<>(key, this, baseSettings, settings);
    }

    @Override
    public void writeDefaultProfile(FileConfig config) {
        config.set("base-settings", EffectBaseSettingsCodec.INSTANCE, DEFAULT_SETTINGS);
    }

    public static Point3D[] getCircleCoordinates(double radius, int circleIndex) {
        Point3D[] coordinates = new Point3D[NUM_POINTS];
        double angle = circleIndex * DELTA_ANGLE;
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        for (int j = 0; j < NUM_POINTS; j++) {
            double theta = j * 2.0 * Math.PI / NUM_POINTS;
            double x = radius * Math.cos(theta) * cosAngle;
            double y = radius * Math.sin(theta) * cosAngle;
            double z = radius * sinAngle;
            coordinates[j] = new Point3D(x, y, z);
        }
        return coordinates;
    }

    @Override
    public void playStep(Location origin, ParticleEffect<?> effect, SphereEffectSettings settings, int step) {
        Point3D[] circlePoints = getCircleCoordinates(1D, step);
        for (int point = 0; point < NUM_POINTS; point++) {
            Point3D point3d = circlePoints[point];
            Location location = origin.clone().add(point3d.x, point3d.z + 0.2, point3d.y);

            effect.play(location);
        }
    }

}
