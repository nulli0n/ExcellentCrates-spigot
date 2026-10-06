package su.nightexpress.excellentcrates.util;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public final class LocaleUtils {

    public static String formatLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        String pattern = Lang.FORMAT_LOCATION.text();

        return pattern
            .replace(SharedPlaceholders.X, NumberUtil.format(x))
            .replace(SharedPlaceholders.Y, NumberUtil.format(y))
            .replace(SharedPlaceholders.Z, NumberUtil.format(z));
    }

    private LocaleUtils() {
    }
}
