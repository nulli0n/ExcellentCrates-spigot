package su.nightexpress.excellentcrates.crates.hologram.component.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;
import su.nightexpress.excellentcrates.crates.hologram.component.StandardHologramComponent;
import su.nightexpress.excellentcrates.crates.hologram.component.data.StandardHologramOffset;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class HologramComponentCodec implements ConfigCodec<StandardHologramComponent> {

    public static final HologramComponentCodec INSTANCE = new HologramComponentCodec();

    @Override
    public StandardHologramComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);
        List<String> text = config.getOrSet(path + ".text", ConfigCodecs.STRING_LIST, List.of());
        HologramOffset yOffset = config.getOrSet(path + ".offset", HologramOffsetCodec.INSTANCE,
            StandardHologramOffset.DEFAULT);

        return new StandardHologramComponent(enabled, text, yOffset);
    }

    @Override
    public void write(FileConfig config, String path, StandardHologramComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".text", value.getText());
        config.set(path + ".offset", value.getOffset());
    }
}
