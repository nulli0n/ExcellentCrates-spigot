package su.nightexpress.excellentcrates.animation.style.csgo.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.animation.style.csgo.CSGOAnimationSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CSGOAnimationSettingsCodec implements ConfigCodec<CSGOAnimationSettings> {

    public static final CSGOAnimationSettingsCodec INSTANCE = new CSGOAnimationSettingsCodec();

    @Override
    public CSGOAnimationSettings read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "CSGO");
        int totalRolls = config.getOrSet(path + ".total_rolls", ConfigCodecs.INT, 16);
        long durationTicks = config.getOrSet(path + ".duration_ticks", ConfigCodecs.LONG, 50L);
        long finishTickDelay = config.getOrSet(path + ".finish_tick_delay", ConfigCodecs.LONG, 20L);

        return new CSGOAnimationSettings(name, totalRolls, durationTicks, finishTickDelay);
    }

    @Override
    public void write(FileConfig config, String path, CSGOAnimationSettings value) {
        config.set(path + ".name", value.name());
        config.set(path + ".total_rolls", value.totalRolls());
        config.set(path + ".duration_ticks", value.durationTicks());
        config.set(path + ".finish_tick_delay", value.finishTickDelay());
    }
}
