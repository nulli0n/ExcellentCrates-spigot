package su.nightexpress.excellentcrates.crates.block.position;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionObserver;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class DefaultCratePositionRegistry implements CratePositionRegistry {

    private final CrateResolver crateResolver;

    private final Map<CratePosition, Identifier>      crateIdByPositionMap;
    private final Map<Identifier, Set<CratePosition>> positionsByCrateIdMap;

    private final List<CratePositionObserver> observers;

    public DefaultCratePositionRegistry(CrateResolver crateResolver) {
        this.crateResolver = crateResolver;
        this.crateIdByPositionMap = new HashMap<>();
        this.positionsByCrateIdMap = new HashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void clear() {
        this.crateIdByPositionMap.clear();
        this.positionsByCrateIdMap.clear();
    }

    @Override
    public void addObserver(CratePositionObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(CratePositionObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void registerPosition(Identifier crateId, CratePosition position) {
        this.crateIdByPositionMap.put(position, crateId);

        Set<CratePosition> existingPositions = this.positionsByCrateIdMap.computeIfAbsent(crateId,
            k -> new HashSet<>());
        existingPositions.add(position);

        this.observers.forEach(observer -> observer.onPositionAdded(crateId, position));
    }

    @Override
    public void unregisterPositions(Crate crate) {
        Identifier crateId = crate.id();

        this.positionsByCrateIdMap.remove(crateId);
        Map.copyOf(this.crateIdByPositionMap).forEach((pos, cachedId) -> {
            if (cachedId.equals(crateId)) {
                this.unregisterPosition(pos); // Trigger observers for this position removal
            }
        });
    }

    @Override
    public void unregisterPosition(CratePosition position) {
        Identifier crateId = this.crateIdByPositionMap.remove(position);
        if (crateId == null) return;

        Set<CratePosition> existingPositions = this.positionsByCrateIdMap.get(crateId);
        if (existingPositions == null) return;

        existingPositions.remove(position);

        if (existingPositions.isEmpty()) {
            this.positionsByCrateIdMap.remove(crateId);
        }

        this.observers.forEach(observer -> observer.onPositionRemoved(crateId, position));
    }

    @Override
    public void unregisterPositions(World world) {
        AdaptedKey worldKey = BukkitKeys.getKey(world);

        this.unregisterPositions(pos -> pos.isWorld(worldKey));
    }

    @Override
    public void unregisterPositions(Chunk chunk) {
        ChunkPos chunkPos = ChunkPos.from(chunk);
        AdaptedKey worldKey = BukkitKeys.getKey(chunk.getWorld());

        this.unregisterPositions(pos -> pos.isWorld(worldKey) && pos.isChunk(chunkPos));
    }

    @Override
    public void unregisterPositions(Predicate<CratePosition> filter) {
        List<CratePosition> positionsToRemove = this.crateIdByPositionMap.keySet()
            .stream()
            .filter(filter)
            .toList();

        for (CratePosition cratePosition : positionsToRemove) {
            this.unregisterPosition(cratePosition);
        }
    }

    @Override
    public @Nullable Crate getCrateAt(Location location) {
        Identifier crateId = this.getCrateIdAt(location);
        return crateId != null ? this.crateResolver.resolveCrate(crateId) : null;
    }

    @Override
    public @Nullable Identifier getCrateIdAt(Location location) {
        AdaptedKey worldKey = BukkitKeys.getKey(location.getWorld());
        ExactPos position = ExactPos.from(location);

        return this.getCrateIdAt(worldKey, position);
    }

    @Override
    public @Nullable Identifier getCrateIdAt(AdaptedKey worldKey, ExactPos position) {
        DefaultCratePosition cratePosition = new DefaultCratePosition(worldKey, position);
        return this.getCrateIdAt(cratePosition);
    }

    @Override
    public @Nullable Identifier getCrateIdAt(CratePosition cratePosition) {
        return this.crateIdByPositionMap.get(cratePosition);
    }

    @Override
    public Set<CratePosition> getCratePositions(Identifier crateId) {
        return Collections.unmodifiableSet(this.positionsByCrateIdMap.getOrDefault(crateId, Set.of()));
    }
}