package su.nightexpress.excellentcrates.crates.block.controller;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.block.position.DefaultCratePosition;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class CratePositionWorldController extends BaseController {

    private final CrateRegistry         crateRegistry;
    private final CratePositionRegistry positionRegistry;

    public CratePositionWorldController(CratesPlugin plugin,
                                        CrateRegistry crateRegistry,
                                        CratePositionRegistry positionRegistry) {
        super(plugin);
        this.crateRegistry = crateRegistry;
        this.positionRegistry = positionRegistry;
    }

    @Override
    protected void onControllerReload() {
        // At this moment, the data service already reloaded all crates.
        // Clean up registry to avoid stale crate positions before re-registering them.
        this.positionRegistry.clear();

        // Re-register all crate positions in all loaded worlds after clearing the registry
        this.registerCratePositionsInAllWorlds();
    }

    @Override
    protected void onControllerShutdown() {
        this.positionRegistry.clear();
    }

    @Override
    protected void onControllerStart() {
        this.registerCratePositionsInAllWorlds();
    }

    private void registerCratePositionsInAllWorlds() {
        this.plugin.getServer().getWorlds().forEach(this::registerCratePositionsInWorld);
    }

    private void registerCratePositionsInWorld(World world) {
        for (Chunk chunk : world.getLoadedChunks()) {
            this.registerCratePositionsInChunk(chunk);
        }
    }

    private void unregisterCratePositionsInWorld(World world) {
        this.positionRegistry.unregisterPositions(world);
    }

    private void registerCratePositionsInChunk(Chunk chunk) {
        World world = chunk.getWorld();
        AdaptedKey worldKey = BukkitKeys.getKey(world);
        ChunkPos chunkPos = ChunkPos.from(chunk);

        this.crateRegistry.values().forEach(crate -> {
            BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);
            if (component == null) return;

            component.getBlockPositions(worldKey)
                .stream()
                .filter(pos -> pos.toChunkPos().equals(chunkPos))
                .map(pos -> new DefaultCratePosition(worldKey, pos))
                .forEach(pos -> this.positionRegistry.registerPosition(crate.id(), pos));
        });
    }

    private void unregisterCratePositionsInChunk(Chunk chunk) {
        this.positionRegistry.unregisterPositions(chunk);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        this.registerCratePositionsInWorld(world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();

        this.unregisterCratePositionsInWorld(world);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        this.registerCratePositionsInChunk(chunk);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();

        this.unregisterCratePositionsInChunk(chunk);
    }
}
