package su.nightexpress.excellentcrates.util.pos;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.NumberUtil;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class WorldPos {

    public static final WorldPos EMPTY = new WorldPos("", BlockPos.empty());

    private final int x, y, z;
    private final String worldName;

    private Reference<World> worldReference;

    public WorldPos(@NotNull String worldName, @NotNull BlockPos blockPos) {
        this(worldName, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public WorldPos(@NotNull String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public static WorldPos empty() {
        return EMPTY.copy();
    }

    @NotNull
    public static WorldPos from(@NotNull Block block) {
        return new WorldPos(block.getWorld().getName(), BlockPos.from(block));
    }

    @NotNull
    public static WorldPos from(@NotNull Location location) {
        return new WorldPos(LocationUtil.getWorldName(location), BlockPos.from(location));
    }

    @NotNull
    public static WorldPos read(@NotNull FileConfig config, @NotNull String path) {
        String str = config.getString(path, "");
        return deserialize(str);
    }

    @NotNull
    public static WorldPos deserialize(@NotNull String str) {
        String[] split = str.split(",");
        if (split.length < 4) return empty();

        int x = (int) NumberUtil.getAnyDouble(split[0], 0);
        int y = (int) NumberUtil.getAnyDouble(split[1], 0);
        int z = (int) NumberUtil.getAnyDouble(split[2], 0);

        String worldName = split[3];

        return new WorldPos(worldName, x, y, z);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @NotNull
    public String serialize() {
        return this.x + "," + this.y + "," + this.z + "," + this.worldName;
    }

    @Nullable
    public World getWorld() {
        if (this.worldReference == null || this.worldReference.get() == null) {
            World world = Bukkit.getWorld(this.worldName);
            if (world != null) {
                this.worldReference = new WeakReference<>(world);
            }
        }
        return this.worldReference == null ? null : this.worldReference.get();
    }

    @NotNull
    public String getWorldName() {
        return worldName;
    }

    @NotNull
    public WorldPos copy() {
        return new WorldPos(this.worldName, this.x, this.y, this.z);
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Nullable
    public Location toLocation() {
        World world = this.getWorld();
        if (world == null) return null;

        return new Location(world, this.x, this.y, this.z);
    }

    @Nullable
    public Block toBlock() {
        World world = this.getWorld();
        if (world == null) return null;

        return world.getBlockAt(this.x, this.y, this.z);
    }

    @Nullable
    public Chunk toChunk() {
        World world = this.getWorld();
        if (world == null) return null;

        int chunkX = this.x >> 4;
        int chunkZ = this.z >> 4;

        return world.getChunkAt(chunkX, chunkZ);
    }

    public boolean isChunkLoaded() {
        World world = this.getWorld();
        if (world == null) return false;

        int chunkX = this.x >> 4;
        int chunkZ = this.z >> 4;

        return world.isChunkLoaded(chunkX, chunkZ);
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof WorldPos other)) return false;
        return x == other.x && y == other.y && z == other.z && Objects.equals(worldName, other.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, worldName);
    }

    @Override
    public String toString() {
        return "WorldPos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", worldName='" + worldName + '\'' +
                '}';
    }
}
