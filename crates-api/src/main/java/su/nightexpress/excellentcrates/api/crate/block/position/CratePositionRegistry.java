package su.nightexpress.excellentcrates.api.crate.block.position;

import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public interface CratePositionRegistry {

    void clear();

    void addObserver(CratePositionObserver observer);

    void removeObserver(CratePositionObserver observer);

    void registerPosition(Identifier crateId, CratePosition position);

    void unregisterPosition(CratePosition position);

    void unregisterPositions(Crate crate);

    void unregisterPositions(World world);

    void unregisterPositions(Chunk chunk);

    void unregisterPositions(Predicate<CratePosition> filter);

    @Nullable
    Crate getCrateAt(Location location);

    @Nullable
    Identifier getCrateIdAt(Location location);

    @Nullable
    Identifier getCrateIdAt(AdaptedKey worldKey, ExactPos position);

    @Nullable
    Identifier getCrateIdAt(CratePosition cratePosition);

    Set<CratePosition> getCratePositions(Identifier crateId);
}
