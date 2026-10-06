package su.nightexpress.excellentcrates.crates.hologram.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.hologram.component.data.StandardHologramOffset;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class HologramOffsetCodec implements ConfigCodec<StandardHologramOffset> {

    public static final HologramOffsetCodec INSTANCE = new HologramOffsetCodec();

    @Override
    public StandardHologramOffset read(FileConfig config, String path) throws CodecReadException {
        double x = config.getOrSet(path + ".x", ConfigCodecs.DOUBLE, 0.0);
        double y = config.getOrSet(path + ".y", ConfigCodecs.DOUBLE, 0.7);
        double z = config.getOrSet(path + ".z", ConfigCodecs.DOUBLE, 0.0);

        return new StandardHologramOffset(x, y, z);
    }

    @Override
    public void write(FileConfig config, String path, StandardHologramOffset value) {
        config.set(path + ".x", value.getX());
        config.set(path + ".y", value.getY());
        config.set(path + ".z", value.getZ());
    }
}
