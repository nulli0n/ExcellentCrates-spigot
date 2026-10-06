package su.nightexpress.excellentcrates.crates.item.interact.controller;

import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.item.interact.ItemInteractionResult;
import su.nightexpress.excellentcrates.api.crate.item.interact.ItemInteractionType;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;
import su.nightexpress.excellentcrates.crates.item.interact.CrateItemInteractionHandler;

@NullMarked
public class CrateItemInteractionController extends BaseController {

    private final CrateItemService            itemService;
    private final CrateItemInteractionHandler handler;

    public CrateItemInteractionController(CratesPlugin plugin,
                                          CrateItemService itemService,
                                          CrateItemInteractionHandler handler) {
        super(plugin);
        this.itemService = itemService;
        this.handler = handler;
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

    private ItemInteractionType getInteractionType(Player player, Action action) {
        boolean isSneaking = player.isSneaking();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            return isSneaking ? ItemInteractionType.SNEAK_RIGHT : ItemInteractionType.RIGHT;
        }
        return isSneaking ? ItemInteractionType.SNEAK_LEFT : ItemInteractionType.LEFT;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCrateItemInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType().isAir()) {
            return;
        }

        if (event.useItemInHand() == Result.DENY) return;

        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemInteractionType type = this.getInteractionType(player, action);

        ActionResult result = this.handler.interactWithCrateItem(player, itemStack, type);
        if (result.reason() == ItemInteractionResult.IGNORE) return;

        event.setCancelled(true);
        event.setUseItemInHand(Result.DENY);
        event.setUseInteractedBlock(Result.DENY);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCrateItemPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (this.itemService.isCrateItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateItemAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        boolean firstResult = first != null && this.itemService.isCrateItem(first);
        boolean secondResult = second != null && this.itemService.isCrateItem(second);

        if (firstResult || secondResult) {
            event.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCrateItemCraft(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.itemService.isCrateItem(item))) {
            event.setCancelled(true);
        }
    }
}
