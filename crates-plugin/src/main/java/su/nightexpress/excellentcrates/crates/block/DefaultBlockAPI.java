package su.nightexpress.excellentcrates.crates.block;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionObserver;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.crates.block.handler.BlockInteractionHandler;
import su.nightexpress.excellentcrates.crates.block.handler.BlockPlacementHandler;

@NullMarked
public class DefaultBlockAPI implements BlockAPI {

    private final BlockRegistry         blockRegistry;
    private final CratePositionRegistry positionRegistry;

    private final BlockPlacementHandler   placementHandler;
    private final BlockInteractionHandler interactionHandler;

    public DefaultBlockAPI(BlockRegistry blockRegistry,
                           CratePositionRegistry positionRegistry,
                           BlockPlacementHandler placementHandler,
                           BlockInteractionHandler interactionHandler) {
        this.blockRegistry = blockRegistry;
        this.positionRegistry = positionRegistry;
        this.placementHandler = placementHandler;
        this.interactionHandler = interactionHandler;
    }

    @Override
    public ActionResult handlePlacement(Identifier source, Player player, ItemStack itemInHand, Location location) {
        return this.placementHandler.handlePlacement(source, player, itemInHand, location);
    }

    @Override
    public ActionResult handleRemoval(Identifier source, Player player, Location location) {
        return this.placementHandler.handleRemoval(source, player, location);
    }

    @Override
    public ActionResult handleInteraction(Identifier sourceId, BlockInteractionType type, Player player,
                                          Location location) {
        return this.interactionHandler.handleInteraction(sourceId, type, player, location);
    }

    @Override
    public void registerProviderWithBlocks(BlockProvider<?> provider) {
        this.blockRegistry.registerProvider(provider);

        provider.fetchBlocks().forEach(this.blockRegistry::registerBlock);
    }

    @Override
    public Set<CrateBlock> unregisterBlocks(BlockProvider<?> provider) {
        return this.blockRegistry.unregisterBlocks(provider);
    }

    @Override
    public void addObserver(CratePositionObserver observer) {
        this.positionRegistry.addObserver(observer);
    }

    @Override
    public void removeObserver(CratePositionObserver observer) {
        this.positionRegistry.removeObserver(observer);
    }

    @Override
    public BlockRegistry getRegistry() {
        return blockRegistry;
    }

    @Override
    public CratePositionRegistry getPositionRegistry() {
        return positionRegistry;
    }
}
