package su.nightexpress.excellentcrates.crates.block.vanilla;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.block.handler.HandlerResult;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;
import su.nightexpress.excellentcrates.crates.block.vanilla.provider.VanillaBlockProvider;

@NullMarked
public class VanillaBlockController extends BaseController {

    private final BlockAPI blocksAPI;

    public VanillaBlockController(CratesPlugin plugin, BlockAPI blocksAPI) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();
        Location location = event.getBlockPlaced().getLocation();

        /* Block blockPlaced = event.getBlockPlaced();
        BlockFace blockFace = BlockFace.UP; // Default to UP if the block face is not available
        if (blockPlaced.getBlockData() instanceof Directional directional) {
            blockFace = directional.getFacing();
        } */

        this.blocksAPI.handlePlacement(VanillaBlockProvider.ID, player, itemInHand, location);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        ActionResult result = this.blocksAPI.handleRemoval(VanillaBlockProvider.ID, player, location);

        if (result.reason() == HandlerResult.DENY) {
            event.setCancelled(true);
        }
    }

    private @Nullable BlockInteractionType getInteractionType(Action action, boolean isSneaking) {
        return switch (action) {
            case LEFT_CLICK_BLOCK -> isSneaking ? BlockInteractionType.SNEAK_LEFT : BlockInteractionType.LEFT;
            case RIGHT_CLICK_BLOCK -> isSneaking ? BlockInteractionType.SNEAK_RIGHT : BlockInteractionType.RIGHT;
            default -> null;
        };
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (event.useInteractedBlock() == Result.DENY) return;
        if (block == null) return;
        if (event.getHand() != EquipmentSlot.HAND) return; // Only handle main hand interactions

        Player player = event.getPlayer();
        Action action = event.getAction();
        boolean isSneaking = player.isSneaking();

        BlockInteractionType type = this.getInteractionType(action, isSneaking);
        if (type == null) return;

        Location location = block.getLocation();

        ActionResult result = this.blocksAPI.handleInteraction(VanillaBlockProvider.ID, type, player,
            location);

        if (result.reason() != HandlerResult.IGNORE) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
        }
    }
}
