package su.nightexpress.excellentcrates.crate.effect;

public class Point3d {

    public double x, y, z;

    public Point3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point3d{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            '}';
    }
}
