package su.nightexpress.engine.bukkit.particle;

import java.util.Objects;
import java.util.Optional;

import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.BukkitThing;

@NullMarked
public record ParticleType<T>(String key, Class<T> dataType) {

    public ParticleType {
        Objects.requireNonNull(key, "Particle key cannot be null");
        Objects.requireNonNull(dataType, "DataType cannot be null");
    }

    public static ParticleType<Void> simple(Particle particle) {
        if (particle.getDataType() != Void.class) {
            throw new IllegalArgumentException(
                "Particle " + particle.name() + " requires data: " + particle.getDataType().getSimpleName()
            );
        }
        return new ParticleType<>(BukkitThing.getAsString(particle), Void.class);
    }

    public static ParticleType<Void> simple(String key) {
        return new ParticleType<>(key, Void.class);
    }

    public static <T> ParticleType<T> withData(Particle particle, Class<T> expectedType) {
        if (!expectedType.isAssignableFrom(particle.getDataType())) {
            throw new IllegalArgumentException("Mismatched data type for " + particle.name() + ". Expected " + particle
                .getDataType().getSimpleName() + ", got " + expectedType.getSimpleName());
        }
        return new ParticleType<>(BukkitThing.getAsString(particle), expectedType);
    }

    public static <T> ParticleType<T> withData(String key, Class<T> expectedType) {
        return new ParticleType<>(key, expectedType);
    }

    public Optional<Particle> getBukkitParticle() {
        return Optional.ofNullable(BukkitThing.getParticle(this.key));
    }

    public ParticleEffect<T> create(T data) {
        return this.builder(data).build();
    }

    public ParticleEffect<Void> create() {
        return this.builder().build();
    }

    public ParticleEffect.Builder<T> builder(T data) {
        return ParticleEffect.builder(this, data);
    }

    public ParticleEffect.Builder<Void> builder() {
        if (dataType != Void.class) {
            throw new IllegalStateException("Particle " + key + " requires data of type " + dataType.getSimpleName());
        }

        return ParticleEffect.builder(simple(this.key));
    }
}