package su.nightexpress.excellentcrates.integration.itemsadder.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;

@NullMarked
public class ItemsAdderBlockPlaceController extends BaseController {

    private final BlockAPI blocksAPI;

    public ItemsAdderBlockPlaceController(CratesPlugin plugin, BlockAPI blocksAPI) {
        super(plugin);
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(CustomBlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();
        Block block = event.getBlock();
        Location location = block.getLocation();

        this.blocksAPI.handlePlacement(ItemsAdderBlockProvider.ID, player, itemInHand, location);
    }
}
