package su.nightexpress.excellentcrates.util;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.Colors2;
import su.nightexpress.excellentcrates.Placeholders;

public abstract class Inspector implements Placeholder {

    public Inspector() {

    }

    @NotNull
    public static String problem(@NotNull String text) {
        return Colorizer.hex(Placeholders.CROSS_MARK + " " + Colors2.GRAY + text);
    }

    @NotNull
    public static String good(@NotNull String text) {
        return Colorizer.hex(Placeholders.CHECK_MARK + " " + Colors2.GRAY + text);
    }

    public static String warning(@NotNull String text) {
        return Colorizer.hex(Placeholders.WARN_MARK + " " + Colors2.GRAY + text);
    }
}
