package su.nightexpress.excellentcrates.crates.data.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateDisplay;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CrateDisplayCodec implements ConfigCodec<StandardCrateDisplay> {

    public static final CrateDisplayCodec INSTANCE = new CrateDisplayCodec();

    @Override
    public StandardCrateDisplay read(FileConfig config, String path) throws CodecReadException {
        String name = config.getString(path + ".name", "Crate");
        List<String> lore = config.getStringList(path + ".lore");

        return new StandardCrateDisplay(name, lore);
    }

    @Override
    public void write(FileConfig config, String path, StandardCrateDisplay value) {
        config.set(path + ".name", value.getName());
        config.set(path + ".lore", value.getLore());
    }
}
