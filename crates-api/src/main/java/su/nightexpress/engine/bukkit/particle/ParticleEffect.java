package su.nightexpress.engine.bukkit.particle;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public record ParticleEffect<T>(ParticleType<T> type,
                                @Nullable T data,
                                int amount,
                                double xOffset,
                                double yOffset,
                                double zOffset,
                                double speed) {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEffect.class);

    public ParticleEffect {
        Objects.requireNonNull(type, "Particle type cannot be null");

        if (type.dataType() != Void.class) {
            Objects.requireNonNull(data, () -> {
                return "Particle " + type.key() + " requires non-null data of type " + type.dataType().getSimpleName();
            });

            if (!type.dataType().isInstance(data)) {
                throw new IllegalArgumentException("Provided data " + data.getClass().getSimpleName() +
                    " does not match expected " + type.dataType().getSimpleName());
            }
        }
    }

    public static <T> Builder<T> builder(ParticleType<T> type, @Nullable T data) {
        return new Builder<>(type, data);
    }

    public static <T> Builder<T> builder(ParticleType<T> type) {
        return new Builder<>(type);
    }

    public static <T> ParticleEffect<T> of(ParticleType<T> type, @Nullable T data) {
        return builder(type, data).build();
    }

    public static ParticleEffect<Void> of(ParticleType<Void> type) {
        return builder(type).build();
    }

    public void play(Location location) {
        Particle bukkitParticle = this.type.getBukkitParticle().orElse(null);
        if (bukkitParticle == null) {
            LOGGER.warn("Particle " + type.key() + " is not recognized");
            return;
        }

        World world = location.getWorld();
        world.spawnParticle(bukkitParticle, location, amount, xOffset, yOffset, zOffset, speed, data);
    }

    public void play(Player player, Location location) {
        Particle bukkitParticle = this.type.getBukkitParticle().orElse(null);
        if (bukkitParticle == null) {
            LOGGER.warn("Particle " + type.key() + " is not recognized");
            return;
        }
        player.spawnParticle(bukkitParticle, location, amount, xOffset, yOffset, zOffset, speed, data);
    }

    public static class Builder<T> {

        private final ParticleType<T> type;
        private @Nullable T           data;
        private int                   amount;
        private double                xOffset;
        private double                yOffset;
        private double                zOffset;
        private double                speed;

        public Builder(ParticleType<T> type) {
            this(type, null);
        }

        public Builder(ParticleType<T> type, @Nullable T data) {
            this.type = type;
            this.data = data;
            this.amount = 1;
            this.speed = 0D;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder<T> xOffset(double xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder<T> yOffset(double yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder<T> zOffset(double zOffset) {
            this.zOffset = zOffset;
            return this;
        }

        public Builder<T> speed(double speed) {
            this.speed = speed;
            return this;
        }

        public ParticleEffect<T> build() {
            return new ParticleEffect<>(type, data, amount, xOffset, yOffset, zOffset, speed);
        }
    }
}