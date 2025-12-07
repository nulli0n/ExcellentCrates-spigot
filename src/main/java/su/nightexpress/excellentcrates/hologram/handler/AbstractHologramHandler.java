package su.nightexpress.excellentcrates.hologram.handler;

import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.hologram.HologramHandler;

public abstract class AbstractHologramHandler implements HologramHandler {

    protected final byte billboard;
    protected final int lineWidth;
    protected final byte  textOpacity;
    protected final byte textBitmask;
    protected final int  backgroundColor;

    public AbstractHologramHandler() {
        this.billboard = translateBillboard(Config.CRATE_HOLOGRAM_BILLBOARD.get());
        this.lineWidth = Integer.MAX_VALUE;
        this.textOpacity = Config.CRATE_HOLOGRAM_TEXT_OPACITY.get().byteValue();
        int[] bgColor = Config.CRATE_HOLOGRAM_BACKGROUND_COLOR.get();
        this.backgroundColor = toARGB(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        this.textBitmask = (byte) ((Config.CRATE_HOLOGRAM_SHADOW.get() ? 0x01 : 0) | (Config.CRATE_HOLOGRAM_SEE_THROUGH.get() ? 0x02 : 0));
    }

    private static int toARGB(int alpha, int red, int green, int blue) {
        return ((alpha & 0xFF) << 24)
            | ((red & 0xFF) << 16)
            | ((green & 0xFF) << 8)
            | (blue & 0xFF);
    }

    private static byte translateBillboard(@NotNull Display.Billboard billboard) {
        return switch (billboard) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
    }
}
