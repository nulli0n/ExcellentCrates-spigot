package su.nightexpress.excellentcrates.integration.packetevents.settings;

import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public final class HologramSettingsSchema {

    public static final ConfigProperty<Billboard> BILLBOARD = ConfigProperty.of(ConfigCodecs.forEnum(Billboard.class),
        "Hologram.Billboard",
        Billboard.VERTICAL,
        "Controls if crate holograms should pivot to face player when rendered.",
        "It can be FIXED (both vertical and horizontal angles are fixed), VERTICAL (faces player around vertical axis), HORIZONTAL (pivots around horizontal axis), and CENTER (pivots around center point).",
        "[Default is " + Display.Billboard.VERTICAL.name() + "]"
    );

    public static final ConfigProperty<Integer> VISIBLE_DISTANCE = ConfigProperty.of(ConfigCodecs.INT,
        "Hologram.Visible-Distance",
        10,
        "Sets crate hologram visibility distance."
    );

    public static final ConfigProperty<Double> SCALE = ConfigProperty.of(ConfigCodecs.DOUBLE,
        "Hologram.Scale",
        0.8,
        "Sets hologram text scale.",
        "[Default is 0.8]"
    );

    public static final ConfigProperty<Integer> LINE_WIDTH = ConfigProperty.of(ConfigCodecs.INT,
        "Hologram.LineWidth",
        200,
        "Maximum line width used to split lines.",
        "[Default is 200]"
    );

    public static final ConfigProperty<Integer> TEXT_OPACITY = ConfigProperty.of(ConfigCodecs.INT,
        "Hologram.TextOpacity",
        -1,
        "Alpha value of rendered text. Value ranges from 0 to 255. Values up to 3 are treated as fully opaque (255).",
        "The text rendering is discarded for values between 4 and 26. Defaults to -1, which represents 255 and is completely opaque.",
        "[Default is -1]"
    );

    public static final ConfigProperty<Boolean> SEE_THROUGH = ConfigProperty.of(ConfigCodecs.BOOLEAN,
        "Hologram.SeeThrough",
        false,
        "Whether the text be visible through blocks.",
        "[Default is false]"
    );

    public static final ConfigProperty<Boolean> SHADOW = ConfigProperty.of(ConfigCodecs.BOOLEAN,
        "Hologram.Shadow",
        true,
        "Whether the text is displayed with shadow.",
        "[Default is true]"
    );

    public static final ConfigProperty<int[]> BACKGROUND_COLOR = ConfigProperty.of(ConfigCodecs.INT_ARRAY,
        "Hologram.BackgroundColor",
        new int[]{64, 0, 0, 0},
        "The background color, arranged by [A,R,G,B]. Where: A = Alpha (opacity), R = Red, G = Green, B = Blue.",
        "[Default is 64,0,0,0]"
    );

    private HologramSettingsSchema() {
    }
}
