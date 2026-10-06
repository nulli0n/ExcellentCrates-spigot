package su.nightexpress.engine.util;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChunkUtil {

    public static long getChunkKey(Chunk chunk) {
        return getChunkKey(chunk.getX(), chunk.getZ());
    }

    public static long getChunkKey(int chunkX, int chunkZ) {
        return chunkX & 0xFFFFFFFFL | (chunkZ & 0xFFFFFFFFL) << 32;
    }

    public static long getChunkKeyOfBlock(Block block) {
        return getChunkKey(block.getX() >> 4, block.getZ() >> 4);
    }

    private ChunkUtil() {
    }
}
