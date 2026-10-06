package su.nightexpress.excellentcrates.effect.util;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EffectUtils {

    public static Location getPointOnCircle(Location location, boolean doCopy, double x, double z, double y) {
        return (doCopy ? location.clone() : location).add(Math.cos(x) * z, y, Math.sin(x) * z);
    }

    private EffectUtils() {
    }
}
