package su.nightexpress.excellentcrates.api.crate.hologram.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface HologramOffset {

    double getX();

    double getY();

    double getZ();

    HologramOffset withX(double x);

    HologramOffset withY(double y);

    HologramOffset withZ(double z);
}
