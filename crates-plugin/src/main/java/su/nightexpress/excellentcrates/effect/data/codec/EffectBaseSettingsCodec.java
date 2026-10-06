package su.nightexpress.excellentcrates.effect.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.bukkit.particle.ParticleTypes;
import su.nightexpress.engine.bukkit.particle.codec.ParticleEffectCodec;
import su.nightexpress.excellentcrates.effect.data.model.DefaultEffectBaseSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class EffectBaseSettingsCodec implements ConfigCodec<DefaultEffectBaseSettings> {

    public static final EffectBaseSettingsCodec INSTANCE = new EffectBaseSettingsCodec();

    @Override
    public DefaultEffectBaseSettings read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "Default");
        ParticleEffect<?> particle = config.getOrSet(path + ".particle",
            ParticleEffectCodec.INSTANCE,
            ParticleTypes.FLAME.create()
        );
        int maxFrames = config.getOrSet(path + ".max_frames", ConfigCodecs.INT, 100);
        int tickInterval = config.getOrSet(path + ".tick_interval", ConfigCodecs.INT, 1);
        int pauseTicks = config.getOrSet(path + ".pause_ticks", ConfigCodecs.INT, 20);

        return new DefaultEffectBaseSettings(name, particle, maxFrames, tickInterval, pauseTicks);
    }

    @Override
    public void write(FileConfig config, String path, DefaultEffectBaseSettings value) {
        config.set(path + ".name", ConfigCodecs.STRING, value.name());
        config.set(path + ".particle", ParticleEffectCodec.INSTANCE, value.effect());
        config.set(path + ".max_frames", ConfigCodecs.INT, value.maxFrames());
        config.set(path + ".tick_interval", ConfigCodecs.INT, value.tickInterval());
        config.set(path + ".pause_ticks", ConfigCodecs.INT, value.pauseTicks());
    }
}
