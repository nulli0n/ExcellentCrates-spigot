package su.nightexpress.excellentcrates.crates.data.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.data.crate.CrateDisplay;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CrateDisplayCodec implements ConfigCodec<CrateDisplay> {

    public static final CrateDisplayCodec INSTANCE = new CrateDisplayCodec();

    @Override
    public CrateDisplay read(FileConfig config, String path) throws CodecReadException {
        String name = config.getString(path + ".name", "Crate");
        List<String> lore = config.getStringList(path + ".lore");

        return new CrateDisplay(name, lore);
    }

    @Override
    public void write(FileConfig config, String path, CrateDisplay value) {
        config.set(path + ".name", value.getName());
        config.set(path + ".lore", value.getLore());
    }
}
