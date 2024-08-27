package su.nightexpress.excellentcrates.util.pos;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.NumberUtil;

public class BlockPos {

    private final int x, y, z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public static BlockPos empty() {
        return new BlockPos(0, 0, 0);
    }

    @NotNull
    public static BlockPos from(@NotNull Block block) {
        return new BlockPos(block.getX(), block.getY(), block.getZ());
    }

    @NotNull
    public static BlockPos from(@NotNull Location location) {
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    public static BlockPos read(@NotNull FileConfig config, @NotNull String path) {
        String str = config.getString(path, "");
        return deserialize(str);
    }

    @NotNull
    public static BlockPos deserialize(@NotNull String str) {
        String[] split = str.split(",");
        if (split.length < 3) return empty();

        int x = (int) NumberUtil.getAnyDouble(split[0], 0);
        int y = (int) NumberUtil.getAnyDouble(split[1], 0);
        int z = (int) NumberUtil.getAnyDouble(split[2], 0);

        return new BlockPos(x, y, z);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @NotNull
    public String serialize() {
        return this.x + "," + this.y + "," + this.z;
    }

    @NotNull
    public Location toLocation(@NotNull World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    @NotNull
    public Block toBlock(@NotNull World world) {
        return world.getBlockAt(this.x, this.y, this.z);
    }

    @NotNull
    public Chunk toChunk(@NotNull World world) {
        int chunkX = this.x >> 4;
        int chunkZ = this.z >> 4;

        return world.getChunkAt(chunkX, chunkZ);
    }

    public boolean isChunkLoaded(@NotNull World world) {
        int chunkX = this.x >> 4;
        int chunkZ = this.z >> 4;

        return world.isChunkLoaded(chunkX, chunkZ);
    }

    @NotNull
    public BlockPos copy() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public boolean isEmpty() {
        return this.x == 0D && this.y == 0D && this.z == 0D;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockPos other)) return false;
        if (this.x != other.x) return false;
        if (this.y != other.y) return false;

        return this.z == other.z;
    }

    public int hashCode() {
        return (this.y + this.z * 31) * 31 + this.x;
    }

    @Override
    public String toString() {
        return "BlockPos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
