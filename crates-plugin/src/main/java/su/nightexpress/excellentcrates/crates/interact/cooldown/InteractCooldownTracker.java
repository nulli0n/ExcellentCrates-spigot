package su.nightexpress.excellentcrates.crates.interact.cooldown;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public class InteractCooldownTracker {

    private final Map<UUID, Map<Identifier, InteractCooldown>> cooldowns;

    public InteractCooldownTracker() {
        this.cooldowns = new ConcurrentHashMap<>();
    }

    private Map<Identifier, InteractCooldown> getCooldowns(UUID playerId) {
        Map<Identifier, InteractCooldown> playerCooldowns = this.cooldowns.get(playerId);
        if (playerCooldowns == null) return Map.of();

        playerCooldowns.values().removeIf(InteractCooldown::hasExpired);

        return playerCooldowns;
    }

    public @Nullable InteractCooldown getCooldown(UUID playerId, Identifier crate) {
        return this.getCooldowns(playerId).get(crate);
    }

    public void setCooldown(UUID playerId, Identifier crate, int durationMillis) {
        this.cooldowns.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .put(crate, new InteractCooldown(System.currentTimeMillis() + durationMillis));
    }

    public void clearCooldown(UUID playerId, Identifier crate) {
        this.getCooldowns(playerId).remove(crate);
    }

    public void clearAllCooldowns(UUID playerId) {
        this.cooldowns.remove(playerId);
    }
}
