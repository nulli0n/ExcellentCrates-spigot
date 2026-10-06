package su.nightexpress.excellentcrates.animation.style.simpleroll.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.animation.style.simpleroll.SimpleRollSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class SimpleRollSettingsCodec implements ConfigCodec<SimpleRollSettings> {

    public static final SimpleRollSettingsCodec INSTANCE = new SimpleRollSettingsCodec();

    @Override
    public SimpleRollSettings read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "Simple Roll");
        int rollAmount = config.getOrSet(path + ".roll_amount", ConfigCodecs.INT, 12);
        int rollInterval = config.getOrSet(path + ".roll_interval", ConfigCodecs.INT, 5);
        int finishDelay = config.getOrSet(path + ".finish_delay", ConfigCodecs.INT, 40);

        return new SimpleRollSettings(name, rollAmount, rollInterval, finishDelay);
    }

    @Override
    public void write(FileConfig config, String path, SimpleRollSettings value) {
        config.set(path + ".name", ConfigCodecs.STRING, value.getName());
        config.set(path + ".roll_amount", ConfigCodecs.INT, value.getRollAmount());
        config.set(path + ".roll_interval", ConfigCodecs.INT, value.getRollInterval());
        config.set(path + ".finish_delay", ConfigCodecs.INT, value.getFinishDelay());
    }
}
