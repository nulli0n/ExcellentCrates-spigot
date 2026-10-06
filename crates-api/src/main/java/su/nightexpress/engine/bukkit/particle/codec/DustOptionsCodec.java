package su.nightexpress.engine.bukkit.particle.codec;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class DustOptionsCodec implements ConfigCodec<Particle.DustOptions> {

    public static final DustOptionsCodec INSTANCE = new DustOptionsCodec();

    @Override
    public DustOptions read(FileConfig config, String path) throws CodecReadException {
        Color color = config.getOrSet(path + ".color", BukkitColorCodec.INSTANCE, Color.WHITE);
        float size = config.getOrSet(path + ".size", ConfigCodecs.DOUBLE, 1D).floatValue();

        return new DustOptions(color, size);
    }

    @Override
    public void write(FileConfig config, String path, DustOptions value) {
        config.set(path + ".color", value.getColor());
        config.set(path + ".size", value.getSize());
    }
}
