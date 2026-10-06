package su.nightexpress.excellentcrates.crates.open.component.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.open.component.DefaultOpeningComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class OpeningComponentCodec implements ConfigCodec<DefaultOpeningComponent> {

    public static final OpeningComponentCodec INSTANCE = new OpeningComponentCodec();

    @Override
    public DefaultOpeningComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);
        List<String> openingCommands = config.getOrSet(path + ".opening_commands", ConfigCodecs.STRING_LIST, List.of());

        return new DefaultOpeningComponent(enabled, openingCommands);
    }

    @Override
    public void write(FileConfig config, String path, DefaultOpeningComponent value) {
        config.set(path + ".enabled", ConfigCodecs.BOOLEAN, value.isEnabled());
        config.set(path + ".opening_commands", ConfigCodecs.STRING_LIST, value.getCommands());
    }
}
