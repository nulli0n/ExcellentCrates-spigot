package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.Point3d;

public class CrateSphereEffect extends CrateEffect {

    private static final double DELTA_ANGLE = Math.PI / 10.0;
    private static final int NUM_CIRCLES = 8;
    private static final int NUM_POINTS = 10;

    public CrateSphereEffect() {
        super(1L, NUM_CIRCLES);
    }

    public static Point3d[] getCircleCoordinates(double radius, int circleIndex) {
        Point3d[] coordinates = new Point3d[NUM_POINTS];
        double angle = circleIndex * DELTA_ANGLE;
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        for (int j = 0; j < NUM_POINTS; j++) {
            double theta = j * 2.0 * Math.PI / NUM_POINTS;
            double x = radius * Math.cos(theta) * cosAngle;
            double y = radius * Math.sin(theta) * cosAngle;
            double z = radius * sinAngle;
            coordinates[j] = new Point3d(x, y, z);
        }
        return coordinates;
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {
        Point3d[] circlePoints = getCircleCoordinates(1D, step);
        for (int j = 0; j < NUM_POINTS; j++) {
            Point3d point3d = circlePoints[j];
            particle.play(location.clone().add(point3d.x, point3d.z + 0.2, point3d.y), 0f, 0f, 1);
        }
    }
}
