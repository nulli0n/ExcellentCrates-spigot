package su.nightexpress.excellentcrates.integration.itemsadder.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import dev.lone.itemsadder.api.Events.FurniturePlacedEvent;
import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.block.handler.HandlerResult;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;

@NullMarked
public class ItemsAdderBlockController extends BaseController {

    private final BlockAPI blocksAPI;

    public ItemsAdderBlockController(CratesPlugin plugin, BlockAPI blocksAPI) {
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

        this.handlePlacement(player, itemInHand, location, event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFurniturePlace(FurniturePlacedEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            return; // No item in hand, ignore the event
        }

        Location location = event.getBukkitEntity().getLocation();

        this.handlePlacement(player, itemInHand, location, event);
    }

    // Removal Events

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        this.handleRemoval(player, location, event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFurnitureBreak(FurnitureBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBukkitEntity().getLocation();

        this.handleRemoval(player, location, event);
    }

    // Interaction Events

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockInteract(CustomBlockInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // Only handle main hand interactions
        }

        Player player = event.getPlayer();
        Block block = event.getBlockClicked();
        Location location = block.getLocation();

        this.handleInteraction(player, location, event);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFurnitureInteract(FurnitureInteractEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBukkitEntity().getLocation();

        this.handleInteraction(player, location, event);
    }

    private ActionResult place(Player player, ItemStack itemInHand, Location location) {
        return this.blocksAPI.handlePlacement(ItemsAdderBlockProvider.ID, player, itemInHand, location);
    }

    private ActionResult remove(Player player, Location location) {
        return this.blocksAPI.handleRemoval(ItemsAdderBlockProvider.ID, player, location);
    }

    private ActionResult interact(Player player, Location location, BlockInteractionType type) {
        return this.blocksAPI.handleInteraction(ItemsAdderBlockProvider.ID, type, player, location);
    }

    private void handlePlacement(Player player, ItemStack itemInHand, Location location, Cancellable event) {
        ActionResult result = this.place(player, itemInHand, location);
        if (result.reason() == HandlerResult.DENY) {
            event.setCancelled(true);
        }
    }

    private void handleRemoval(Player player, Location location, Cancellable event) {
        ActionResult interactionResult = this.interact(player, location, BlockInteractionType.LEFT); // Left click for removal
        if (interactionResult.reason() != HandlerResult.IGNORE) {
            event.setCancelled(true);
            return;
        }

        ActionResult removalResult = this.remove(player, location);
        if (removalResult.reason() == HandlerResult.DENY) {
            event.setCancelled(true);
        }
    }

    private void handleInteraction(Player player, Location location, Cancellable event) {
        boolean isSneaking = player.isSneaking();
        BlockInteractionType type = isSneaking ? BlockInteractionType.SNEAK_RIGHT : BlockInteractionType.RIGHT;

        ActionResult result = this.interact(player, location, type);
        if (result.reason() != HandlerResult.IGNORE) {
            event.setCancelled(true);
        }
    }
}
