package su.nightexpress.excellentcrates.rarity.data.codec;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarityBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RarityBaseCodec implements ConfigCodec<StandardRarityBase> {

    public static final RarityBaseCodec INSTANCE = new RarityBaseCodec();

    @Override
    public StandardRarityBase read(FileConfig config, String path) throws CodecReadException {
        String name = config.getString(path + ".name", "Rarity");
        double weight = config.getDouble(path + ".weight", 0D);
        Color color = config.getOrSet(path + ".color", BukkitColorCodec.INSTANCE, Color.WHITE);

        return new StandardRarityBase(name, weight, color);
    }

    @Override
    public void write(FileConfig config, String path, StandardRarityBase value) {
        config.set(path + ".name", value.getName());
        config.set(path + ".weight", value.getWeight());
        config.set(path + ".color", value.getColor());
    }
}
