package su.nightexpress.excellentcrates.animation.visibility;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Owns the entities and cosmetic effects of one built-in opening animation. */
@NullMarked
public class AnimationAudience {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationAudience.class);

    private static final int CELEBRATION_PARTICLES = 40;

    private final Plugin      plugin;
    private final Player      player;
    private final Set<Entity> entities = new HashSet<>();

    public AnimationAudience(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public <T extends Entity> T spawn(Location location, Class<T> type, Consumer<T> initializer) {
        try {
            T entity = location.getWorld().spawn(location, type, spawned -> {
                // The consumer runs before world insertion and client tracking. Late viewers
                // also remain excluded because animation entities are never visible by default.
                this.entities.add(spawned);
                initializer.accept(spawned);
                spawned.setPersistent(false);
                spawned.setVisibleByDefault(false);
            });

            this.player.showEntity(this.plugin, entity);
            return entity;
        }
        catch (RuntimeException exception) {
            // Also remove earlier roulette entries if creating an item/text pair fails.
            this.cleanup();
            throw exception;
        }
    }

    public void remove(Entity entity) {
        entity.remove();
        this.entities.remove(entity);
    }

    public void cleanup() {
        for (Entity entity : Set.copyOf(this.entities)) {
            try {
                this.remove(entity);
            }
            catch (RuntimeException exception) {
                LOGGER.error("Failed to remove an opening animation entity", exception);
            }
        }
    }

    public void playSound(Location location, Sound sound, float volume) {
        this.player.playSound(location, sound, volume, 1f);
    }

    public void celebrate(Location location) {
        // A player-targeted burst avoids a world firework's sound, explosion and damage.
        this.player.spawnParticle(Particle.FIREWORK, location, CELEBRATION_PARTICLES, 0.3, 0.3, 0.3, 0.1);
    }
}
