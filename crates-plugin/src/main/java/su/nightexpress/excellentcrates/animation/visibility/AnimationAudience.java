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

import su.nightexpress.excellentcrates.core.util.FireworkUtil;
import su.nightexpress.nightcore.util.sound.VanillaSound;

/** Owns the entities and cosmetic effects of one built-in opening animation. */
@NullMarked
public class AnimationAudience {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationAudience.class);

    private static final int CELEBRATION_PARTICLES = 40;

    private final Plugin              plugin;
    private final Player              player;
    private final AnimationVisibility visibility;
    private final Set<Entity>         entities = new HashSet<>();

    public AnimationAudience(Plugin plugin, Player player, AnimationVisibility visibility) {
        this.plugin = plugin;
        this.player = player;
        this.visibility = visibility;
    }

    public <T extends Entity> T spawn(Location location, Class<T> type, Consumer<T> initializer) {
        T entity = location.getWorld().spawn(location, type, spawned -> {
            // The consumer runs before world insertion and client tracking. Late viewers
            // also remain excluded because PRIVATE entities are never visible by default.
            this.entities.add(spawned);
            spawned.setPersistent(false);
            if (this.visibility == AnimationVisibility.PRIVATE) {
                spawned.setVisibleByDefault(false);
            }
            initializer.accept(spawned);
        });

        if (this.visibility == AnimationVisibility.PRIVATE) {
            this.player.showEntity(this.plugin, entity);
        }
        return entity;
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
        if (this.visibility == AnimationVisibility.PRIVATE) {
            this.player.playSound(location, sound, volume, 1f);
        }
        else {
            VanillaSound.of(sound, volume).play(location);
        }
    }

    public void celebrate(Location location) {
        if (this.visibility == AnimationVisibility.PRIVATE) {
            // A player-targeted burst avoids a world firework's sound, explosion and damage.
            this.player.spawnParticle(Particle.FIREWORK, location, CELEBRATION_PARTICLES, 0.3, 0.3, 0.3, 0.1);
        }
        else {
            FireworkUtil.createRandom(location);
        }
    }
}
