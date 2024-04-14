package su.nightexpress.excellentcrates.key;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.nightcore.manager.AbstractListener;

import java.util.stream.Stream;

public class KeyListener extends AbstractListener<CratesPlugin> {

    private final KeyManager keyManager;

    public KeyListener(@NotNull CratesPlugin plugin, @NotNull KeyManager keyManager) {
        super(plugin);
        this.keyManager = keyManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.keyManager.giveKeysOnHold(player);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKeyPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        event.setCancelled(this.keyManager.isKey(item));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyUse(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.useInteractedBlock() == Event.Result.DENY) return;

        ItemStack item = event.getItem();
        if (item != null && this.keyManager.isKey(item)) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType().isInteractable() && !player.isSneaking()) {
                return;
            }

            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyAnvilStop(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if ((first != null && this.keyManager.isKey(first)) || (second != null && this.keyManager.isKey(second))) {
            event.setResult(null);
        }
    }

    private boolean doesRecipeContainKeys(Recipe recipe) {
        Stream<RecipeChoice> choices;
        if (recipe instanceof ShapedRecipe shaped) {
            choices = shaped.getChoiceMap().values().stream();
        } else if (recipe instanceof ShapelessRecipe shapeless) {
            choices = shapeless.getChoiceList().stream();
        } else {
            return false;
        }
        return choices.filter(RecipeChoice.ExactChoice.class::isInstance)
                .map(RecipeChoice.ExactChoice.class::cast)
                .flatMap(choice -> choice.getChoices().stream())
                .anyMatch(this.keyManager::isKey);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKeyCraftStop(CraftItemEvent event) {
        // Allow using keys in recipes that contain them specifically.
        if (doesRecipeContainKeys(event.getRecipe())) {
            return;
        }
        CraftingInventory inventory = event.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.keyManager.isKey(item))) {
            event.setCancelled(true);
        }
    }
}
