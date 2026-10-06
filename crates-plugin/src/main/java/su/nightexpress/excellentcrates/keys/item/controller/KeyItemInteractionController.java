package su.nightexpress.excellentcrates.keys.item.controller;

import java.util.stream.Stream;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.keys.item.KeyItemService;

@NullMarked
public class KeyItemInteractionController extends BaseController {

    private final KeyItemService itemService;

    public KeyItemInteractionController(CratesPlugin plugin, KeyItemService itemService) {
        super(plugin);
        this.itemService = itemService;
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

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKeyPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (this.itemService.isKeyItem(item)) {
            event.setCancelled(true);
        }
    }

    /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyUse(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.useInteractedBlock() == Event.Result.DENY) return;
    
        ItemStack item = event.getItem();
        if (item != null && this.itemService.isKeyItem(item)) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType().isInteractable() && !player.isSneaking()) {
                return;
            }
    
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    } */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyAnvilStop(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        boolean firstIsKey = first != null && this.itemService.isKeyItem(first);
        boolean secondIsKey = second != null && this.itemService.isKeyItem(second);

        if (firstIsKey || secondIsKey) {
            event.setResult(null);
        }
    }

    private boolean doesRecipeContainKeys(Recipe recipe) {
        Stream<RecipeChoice> choices;
        if (recipe instanceof ShapedRecipe shaped) {
            choices = shaped.getChoiceMap().values().stream();
        }
        else if (recipe instanceof ShapelessRecipe shapeless) {
            choices = shapeless.getChoiceList().stream();
        }
        else {
            return false;
        }
        return choices.filter(RecipeChoice.ExactChoice.class::isInstance)
            .map(RecipeChoice.ExactChoice.class::cast)
            .flatMap(choice -> choice.getChoices().stream())
            .anyMatch(this.itemService::isKeyItem);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKeyCraftStop(CraftItemEvent event) {
        // Allow using keys in recipes that contain them specifically.
        if (doesRecipeContainKeys(event.getRecipe())) {
            return;
        }

        CraftingInventory inventory = event.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.itemService.isKeyItem(item))) {
            event.setCancelled(true);
        }
    }
}
