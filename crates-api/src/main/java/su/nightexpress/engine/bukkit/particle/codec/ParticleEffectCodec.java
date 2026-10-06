package su.nightexpress.engine.bukkit.particle.codec;

import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.engine.bukkit.particle.ParticleType;
import su.nightexpress.engine.bukkit.particle.ParticleTypes;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.BukkitThing;

@NullMarked
public class ParticleEffectCodec implements ConfigCodec<ParticleEffect<?>> {

    public static final ParticleEffectCodec INSTANCE = new ParticleEffectCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEffectCodec.class);

    @Override
    public ParticleEffect<?> read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, ParticleTypes.CLOUD.key());
        Particle particle = BukkitThing.getParticle(name);
        if (particle == null) {
            LOGGER.error("Invalid particle name: '{}', fallback to default particle.", name);
            particle = Particle.CLOUD;
        }

        Class<?> dataType = particle.getDataType();
        if (dataType == Void.class) {
            return ParticleType.simple(particle).create();
        }

        return this.readEffect(particle, dataType, config, path);
    }

    @Override
    public void write(FileConfig config, String path, ParticleEffect<?> value) {
        Particle particle = value.type().getBukkitParticle().orElse(null);
        if (particle == null) {
            LOGGER.error("Invalid particle in effect: '{}', fallback to default particle.", value.type().key());
            return;
        }

        config.set(path + ".name", BukkitThing.getValue(particle));

        if (particle.getDataType() != Void.class) {
            config.set(path + ".data", value.data());
        }
    }

    private <T> ParticleEffect<?> readEffect(Particle particle, Class<T> dataType, FileConfig config, String path) {
        ConfigCodec<T> codec = ConfigCodecs.getCodec(dataType);
        if (codec == null) {
            LOGGER.error("No codec found for particle data type: '{}', fallback to default particle.",
                dataType.getName()
            );
            return ParticleType.simple(Particle.CLOUD).create();
        }

        T data = config.get(path + ".data", codec);
        if (data == null) {
            LOGGER.error("Failed to read particle data for type: '{}', fallback to default particle.",
                dataType.getName()
            );
            return ParticleType.simple(Particle.CLOUD).create();
        }

        ParticleType<T> typedKey = ParticleType.withData(particle, dataType);
        return ParticleEffect.of(typedKey, data);
    }
}
