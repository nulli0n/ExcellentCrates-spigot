package su.nightexpress.excellentcrates.integration.itemsadder.block;

import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;

@NullMarked
public class ItemsAdderLoadController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsAdderLoadController.class);

    private final ItemsAdderBlockProvider provider;
    private final BlockAPI                blocksAPI;

    public ItemsAdderLoadController(CratesPlugin plugin, ItemsAdderBlockProvider provider, BlockAPI blocksAPI) {
        super(plugin);
        this.provider = provider;
        this.blocksAPI = blocksAPI;
    }

    @Override
    protected void onControllerReload() {

    }

    @Override
    protected void onControllerShutdown() {

    }

    @Override
    protected void onControllerStart() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemsAdderLoad(ItemsAdderLoadDataEvent event) {
        Set<CrateBlock> unregistered = this.blocksAPI.unregisterBlocks(this.provider);
        Set<ItemsAdderBlock> loadedBlocks = this.provider.fetchBlocks();

        loadedBlocks.forEach(block -> this.blocksAPI.getRegistry().registerBlock(block));

        LOGGER.info("Unregistered {} ItemsAdder blocks.", unregistered.size());
        LOGGER.info("Registered {} ItemsAdder blocks.", loadedBlocks.size());
    }
}
