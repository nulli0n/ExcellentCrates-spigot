package su.nightexpress.excellentcrates.keys.data.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.data.key.StandardKeyDisplay;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class KeyDisplayCodec implements ConfigCodec<StandardKeyDisplay> {

    public static final KeyDisplayCodec INSTANCE = new KeyDisplayCodec();

    @Override
    public StandardKeyDisplay read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "A key");
        List<String> lore = config.getOrSet(path + ".lore", ConfigCodecs.STRING_LIST, List.of(
            "A key to open a crate."));

        return new StandardKeyDisplay(name, lore);
    }

    @Override
    public void write(FileConfig config, String path, StandardKeyDisplay value) {
        config.set(path + ".name", value.getName());
        config.set(path + ".lore", value.getLore());
    }
}
