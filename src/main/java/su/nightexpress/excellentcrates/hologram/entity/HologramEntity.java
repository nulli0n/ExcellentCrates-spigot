package su.nightexpress.excellentcrates.hologram.entity;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.util.pos.WorldPos;

import java.util.Set;
import java.util.UUID;

public record HologramEntity(int entityID, WorldPos position, String text, double gap, Set<UUID> players) {

    public void addPlayer(@NotNull Player player) {
        this.players.add(player.getUniqueId());
    }

    public void removePlayer(@NotNull Player player) {
        this.players.remove(player.getUniqueId());
    }

    public boolean isCreated(@NotNull Player player) {
        return this.players.contains(player.getUniqueId());
    }
}
