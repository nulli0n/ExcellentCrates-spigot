package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.Point3d;

public class CrateHeartEffect extends CrateEffect {

    private static final int POINTS = 20;

    private boolean rotate = false;

    public CrateHeartEffect() {
        super(1L, POINTS);
    }

    /*public static List<Point3d> generatePoints(int numPoints) {
        List<Point3d> points = new ArrayList<>();
        double delta = Math.PI / numPoints;
        double angle = 0;
        double x, y, z;
        for (int i = 0; i < numPoints; i++) {
            z = 16 * Math.pow(Math.sin(angle), 3);
            y = 13 * Math.cos(angle) - 5 * Math.cos(2 * angle) - 2 * Math.cos(3 * angle) - Math.cos(4 * angle);
            x = 0;
            points.add(new Point3d(x / 25, (y / 25) - 1.8, z / 25));
            angle += delta;
        }

        List<Point3d> mirroredPoints = new ArrayList<>(points.size());
        for (int i = points.size() - 1; i >= 0; i--) {
            Point3d point = points.get(i);
            mirroredPoints.add(new Point3d(point.x, point.y, -point.z));
        }

        points.addAll(mirroredPoints);
        return points;
    }*/

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {
        boolean isX = this.rotate;
        location = location.add(0, 4, 0);

        double delta = Math.PI / POINTS;
        double angle = delta * step;
        double z = 16 * Math.pow(Math.sin(angle), 3);
        double y = 13 * Math.cos(angle) - 5 * Math.cos(2 * angle) - 2 * Math.cos(3 * angle) - Math.cos(4 * angle);
        double x = 0;

        double realX = isX ? z : x;
        double realZ = isX ? x : z;
        Point3d point = new Point3d(realX / 25, (y / 25) - 1.8, realZ / 25);
        Point3d mirrored = new Point3d(isX ? -point.x : point.x, point.y, isX ? point.z : -point.z);
        particle.play(location.clone().add(point.x, point.y, point.z), 0f, 0f, 1);
        particle.play(location.clone().add(mirrored.x, mirrored.y, mirrored.z), 0f, 0f, 1);

        if (step == 0) {
            this.rotate = !this.rotate;
        }
    }
}
