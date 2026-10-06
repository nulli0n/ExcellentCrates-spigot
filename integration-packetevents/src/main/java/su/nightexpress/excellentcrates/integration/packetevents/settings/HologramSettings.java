package su.nightexpress.excellentcrates.integration.packetevents.settings;

import org.bukkit.entity.Display.Billboard;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public record HologramSettings(Billboard billboard,
                               boolean shadow,
                               boolean seeThrough,
                               double scale,
                               int lineWidth,
                               int textOpacity,
                               int[] backgroundColor,
                               double visibleDistance) {

    public static HologramSettings defaults() {
        Billboard defaultBillboard = HologramSettingsSchema.BILLBOARD.getDefaultValue();
        boolean defaultShadow = HologramSettingsSchema.SHADOW.getDefaultValue();
        boolean defaultSeeThrough = HologramSettingsSchema.SEE_THROUGH.getDefaultValue();
        double defaultScale = HologramSettingsSchema.SCALE.getDefaultValue();
        int defaultLineWidth = HologramSettingsSchema.LINE_WIDTH.getDefaultValue();
        int defaultTextOpacity = HologramSettingsSchema.TEXT_OPACITY.getDefaultValue();
        int[] defaultBackgroundColor = HologramSettingsSchema.BACKGROUND_COLOR.getDefaultValue();
        double defaultVisibleDistance = HologramSettingsSchema.VISIBLE_DISTANCE.getDefaultValue();

        return new HologramSettings(
            defaultBillboard,
            defaultShadow,
            defaultSeeThrough,
            defaultScale,
            defaultLineWidth,
            defaultTextOpacity,
            defaultBackgroundColor,
            defaultVisibleDistance
        );
    }

    public static HologramSettings loadFrom(FileConfig config) {
        Billboard billboard = config.getOrSet(HologramSettingsSchema.BILLBOARD);
        boolean shadow = config.getOrSet(HologramSettingsSchema.SHADOW);
        boolean seeThrough = config.getOrSet(HologramSettingsSchema.SEE_THROUGH);
        double scale = config.getOrSet(HologramSettingsSchema.SCALE);
        int lineWidth = config.getOrSet(HologramSettingsSchema.LINE_WIDTH);
        int textOpacity = config.getOrSet(HologramSettingsSchema.TEXT_OPACITY);
        int[] backgroundColor = config.getOrSet(HologramSettingsSchema.BACKGROUND_COLOR);
        double visibleDistance = config.getOrSet(HologramSettingsSchema.VISIBLE_DISTANCE);

        return new HologramSettings(
            billboard,
            shadow,
            seeThrough,
            scale,
            lineWidth,
            textOpacity,
            backgroundColor,
            visibleDistance
        );
    }
}
