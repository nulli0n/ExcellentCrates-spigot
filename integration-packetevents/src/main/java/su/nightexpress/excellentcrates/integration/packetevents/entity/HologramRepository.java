package su.nightexpress.excellentcrates.integration.packetevents.entity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;

@NullMarked
public class HologramRepository {

    private final Map<CratePosition, Hologram> byWorldMap;
    private final Set<UUID>                    ignoredViewers;

    public HologramRepository() {
        this.byWorldMap = new ConcurrentHashMap<>();
        this.ignoredViewers = ConcurrentHashMap.newKeySet();
    }

    public void addHologram(CratePosition cratePosition, Hologram hologram) {
        this.byWorldMap.put(cratePosition, hologram);
    }

    public boolean isIgnoredViewer(UUID viewer) {
        return this.ignoredViewers.contains(viewer);
    }

    public void addIgnoredViewer(UUID viewer) {
        this.ignoredViewers.add(viewer);
    }

    public void removeIgnoredViewer(UUID viewer) {
        this.ignoredViewers.remove(viewer);
    }

    public Set<UUID> getIgnoredViewers() {
        return Collections.unmodifiableSet(this.ignoredViewers);
    }

    public Set<Hologram> getAll() {
        return Collections.unmodifiableSet(this.byWorldMap.values().stream().collect(Collectors.toSet()));
    }

    public Set<Integer> getHologramEntityIds() {
        return this.getAll().stream().mapToInt(Hologram::getEntityId).boxed().collect(Collectors.toSet());
    }

    public @Nullable Hologram getHologram(CratePosition cratePosition) {
        return this.byWorldMap.get(cratePosition);
    }
}
