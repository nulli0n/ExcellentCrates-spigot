package su.nightexpress.engine.bukkit.codec;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class BukkitColorCodec implements ConfigCodec<Color> {

    public static final BukkitColorCodec INSTANCE = new BukkitColorCodec();

    @Override
    public Color read(FileConfig config, String path) throws CodecReadException {
        int red = config.getOrSet(path + ".red", ConfigCodecs.INT, 255);
        int green = config.getOrSet(path + ".green", ConfigCodecs.INT, 255);
        int blue = config.getOrSet(path + ".blue", ConfigCodecs.INT, 255);
        int alpha = config.getOrSet(path + ".alpha", ConfigCodecs.INT, 255);

        return Color.fromARGB(alpha, red, green, blue);
    }

    @Override
    public void write(FileConfig config, String path, Color value) {
        config.set(path + ".red", value.getRed());
        config.set(path + ".green", value.getGreen());
        config.set(path + ".blue", value.getBlue());
        config.set(path + ".alpha", value.getAlpha());
    }


}
