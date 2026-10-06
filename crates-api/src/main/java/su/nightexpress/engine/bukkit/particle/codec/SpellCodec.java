package su.nightexpress.engine.bukkit.particle.codec;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.Spell;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class SpellCodec implements ConfigCodec<Particle.Spell> {

    public static final SpellCodec INSTANCE = new SpellCodec();

    @Override
    public Spell read(FileConfig config, String path) throws CodecReadException {
        Color color = config.getOrSet(path + ".color", BukkitColorCodec.INSTANCE, Color.WHITE);
        float power = config.getOrSet(path + ".power", ConfigCodecs.DOUBLE, 1D).floatValue();

        return new Particle.Spell(color, power);
    }

    @Override
    public void write(FileConfig config, String path, Spell value) {
        config.set(path + ".color", BukkitColorCodec.INSTANCE, value.getColor());
        config.set(path + ".power", ConfigCodecs.DOUBLE, (double) value.getPower());
    }
}
