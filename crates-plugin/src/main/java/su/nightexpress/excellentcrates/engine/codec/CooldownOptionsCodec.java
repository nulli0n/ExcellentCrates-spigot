package su.nightexpress.excellentcrates.engine.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownMode;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CooldownOptionsCodec implements ConfigCodec<CooldownOptions> {

    public static final CooldownOptionsCodec INSTANCE = new CooldownOptionsCodec();

    @Override
    public CooldownOptions read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);
        CooldownMode mode = config.getOrSet(path + ".mode", ConfigCodecs.forEnum(CooldownMode.class),
            CooldownMode.DAILY);
        long cooldown = config.getOrSet(path + ".value", ConfigCodecs.LONG, 0L);

        return new CooldownOptions(enabled, mode, cooldown);
    }

    @Override
    public void write(FileConfig config, String path, CooldownOptions value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".mode", value.getMode().name());
        config.set(path + ".value", value.getDuration());
    }
}
