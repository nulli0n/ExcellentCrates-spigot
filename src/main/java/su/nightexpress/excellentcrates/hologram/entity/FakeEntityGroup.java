package su.nightexpress.excellentcrates.hologram.entity;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.util.pos.WorldPos;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class FakeEntityGroup {

    private final WorldPos         blockPos;
    private final List<FakeEntity> entities;
    private final Set<UUID>        humanViewers;

    private volatile boolean disabled;

    public FakeEntityGroup(@NotNull WorldPos blockPos) {
        this.blockPos = blockPos;
        this.entities = new CopyOnWriteArrayList<>();
        this.humanViewers = ConcurrentHashMap.newKeySet();
        this.disabled = false;
    }

    public void addViewer(@NotNull Player player) {
        this.humanViewers.add(player.getUniqueId());
    }

    public void removeViewer(@NotNull Player player) {
        this.removeViewer(player.getUniqueId());
    }

    public void removeViewer(@NotNull UUID playerId) {
        this.humanViewers.remove(playerId);
    }

    @NotNull
    public Set<UUID> getViewerIds() {
        return this.humanViewers;
    }

    public boolean isViewer(@NotNull Player player) {
        return this.humanViewers.contains(player.getUniqueId());
    }

    public void clearViewers() {
        this.humanViewers.clear();
    }

    public void addEntity(@NotNull FakeEntity entity) {
        this.entities.add(entity);
    }

    @NotNull
    public List<FakeEntity> getEntities() {
        return this.entities;
    }

    @NotNull
    public Set<Integer> getEntityIDs() {
        return this.entities.stream().map(FakeEntity::getId).collect(Collectors.toSet());
    }

    @NotNull
    public WorldPos getBlockPosition() {
        return this.blockPos;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
