package su.nightexpress.excellentcrates.keys.cost.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.cost.component.model.StandardKeyRequirementEntry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class KeyCostEntryCodec implements ConfigCodec<StandardKeyRequirementEntry> {

    public static final KeyCostEntryCodec INSTANCE = new KeyCostEntryCodec();

    @Override
    public StandardKeyRequirementEntry read(FileConfig config, String path) throws CodecReadException {
        int amount = config.getOrSet(path + ".amount", ConfigCodecs.INT, 0);

        return new StandardKeyRequirementEntry(amount);
    }

    @Override
    public void write(FileConfig config, String path, StandardKeyRequirementEntry value) {
        config.set(path + ".amount", value.getAmount());
    }
}
