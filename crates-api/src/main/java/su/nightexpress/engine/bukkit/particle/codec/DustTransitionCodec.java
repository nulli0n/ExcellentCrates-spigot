package su.nightexpress.engine.bukkit.particle.codec;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustTransition;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class DustTransitionCodec implements ConfigCodec<Particle.DustTransition> {

    public static final DustTransitionCodec INSTANCE = new DustTransitionCodec();

    @Override
    public DustTransition read(FileConfig config, String path) throws CodecReadException {
        Color fromColor = config.getOrSet(path + ".color_from", BukkitColorCodec.INSTANCE, Color.WHITE);
        Color toColor = config.getOrSet(path + ".color_to", BukkitColorCodec.INSTANCE, Color.WHITE);
        double size = config.getDouble(path + ".size", 1D);
        return new DustTransition(fromColor, toColor, (float) size);
    }

    @Override
    public void write(FileConfig config, String path, DustTransition value) {
        config.set(path + ".color_from", BukkitColorCodec.INSTANCE, value.getColor());
        config.set(path + ".color_to", BukkitColorCodec.INSTANCE, value.getToColor());
        config.set(path + ".size", value.getSize());
    }
}
