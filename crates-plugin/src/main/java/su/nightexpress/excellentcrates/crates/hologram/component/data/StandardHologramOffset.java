package su.nightexpress.excellentcrates.crates.hologram.component.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;

@NullMarked
public class StandardHologramOffset implements HologramOffset {

    public static final StandardHologramOffset DEFAULT = new StandardHologramOffset(0, 0.7, 0);

    private final double x;
    private final double y;
    private final double z;

    public StandardHologramOffset(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public HologramOffset withX(double x) {
        return new StandardHologramOffset(x, this.y, this.z);
    }

    @Override
    public HologramOffset withY(double y) {
        return new StandardHologramOffset(this.x, y, this.z);
    }

    @Override
    public HologramOffset withZ(double z) {
        return new StandardHologramOffset(this.x, this.y, z);
    }
}
