package su.nightexpress.excellentcrates.integration.packetevents.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Hologram {

    private final int      entityId;
    private final Location displayLocation;

    private final Set<UUID> humanViewers;

    public Hologram(int entityId, Location displayLocation) {
        this.entityId = entityId;
        this.displayLocation = displayLocation;
        this.humanViewers = new HashSet<>();
    }

    public void addViewer(Player player) {
        this.humanViewers.add(player.getUniqueId());
    }

    public void removeViewer(Player player) {
        this.humanViewers.remove(player.getUniqueId());
    }

    public boolean isViewer(Player player) {
        return this.humanViewers.contains(player.getUniqueId());
    }

    public void clearViewers() {
        this.humanViewers.clear();
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Location getDisplayLocation() {
        return this.displayLocation;
    }

    public Set<UUID> getHumanViewers() {
        return this.humanViewers;
    }
}
