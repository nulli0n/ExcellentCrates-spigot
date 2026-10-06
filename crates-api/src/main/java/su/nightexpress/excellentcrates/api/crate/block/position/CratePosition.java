package su.nightexpress.excellentcrates.api.crate.block.position;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public interface CratePosition {

    AdaptedKey worldKey();

    ExactPos position();

    default boolean isWorld(AdaptedKey worldKey) {
        return this.worldKey().equals(worldKey);
    }

    default boolean isChunk(ChunkPos chunkPos) {
        return this.position().toChunkPos().equals(chunkPos);
    }
}
