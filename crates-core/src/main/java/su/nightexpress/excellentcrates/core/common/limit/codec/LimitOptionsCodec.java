package su.nightexpress.excellentcrates.core.common.limit.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.common.limit.DefaultLimitOptions;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class LimitOptionsCodec implements ConfigCodec<DefaultLimitOptions> {

    public static final LimitOptionsCodec INSTANCE = new LimitOptionsCodec();

    @Override
    public DefaultLimitOptions read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".Enabled", ConfigCodecs.BOOLEAN, false);
        int amount = config.getOrSet(path + ".Amount", ConfigCodecs.INT, 0);

        return new DefaultLimitOptions(enabled, amount);
    }

    @Override
    public void write(FileConfig config, String path, DefaultLimitOptions value) {
        config.set(path + ".Enabled", value.isEnabled());
        config.set(path + ".Amount", value.getAmount());
    }
}
