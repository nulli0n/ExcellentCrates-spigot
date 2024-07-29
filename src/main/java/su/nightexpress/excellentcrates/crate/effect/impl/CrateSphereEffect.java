package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.excellentcrates.util.pos.Point3D;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CrateSphereEffect extends AbstractEffect {

    private static final double DELTA_ANGLE = Math.PI / 10.0;
    private static final int NUM_CIRCLES = 8;
    private static final int NUM_POINTS = 10;

    public CrateSphereEffect() {
        super(1L, NUM_CIRCLES);
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
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step) {
        Point3D[] circlePoints = getCircleCoordinates(1D, step);
        for (int j = 0; j < NUM_POINTS; j++) {
            Point3D point3d = circlePoints[j];
            Location location = origin.clone().add(point3d.x, point3d.z + 0.2, point3d.y);

            playSafe(location, player -> particle.play(player, location, 0f, 0f, 1));
        }
    }
}
