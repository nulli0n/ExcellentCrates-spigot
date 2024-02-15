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
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;

import java.util.stream.Stream;

public class KeyListener extends AbstractListener<ExcellentCratesPlugin> {

    private final KeyManager keyManager;

    public KeyListener(@NotNull KeyManager keyManager) {
        super(keyManager.plugin());
        this.keyManager = keyManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUserLoad(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.keyManager.giveKeysOnHold(player);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKeyPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        e.setCancelled(this.keyManager.isKey(item));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyUse(PlayerInteractEvent e) {
        if (e.useItemInHand() == Event.Result.DENY) return;
        if (e.useInteractedBlock() == Event.Result.DENY) return;

        ItemStack item = e.getItem();
        if (item != null && this.keyManager.isKey(item)) {

            Player player = e.getPlayer();
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType().isInteractable() && !player.isSneaking()) {
                return;
            }

            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeyAnvilStop(PrepareAnvilEvent e) {
        AnvilInventory inventory = e.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if ((first != null && this.keyManager.isKey(first)) || (second != null && this.keyManager.isKey(second))) {
            e.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKeyCraftShop(CraftItemEvent e) {
        CraftingInventory inventory = e.getInventory();
        if (Stream.of(inventory.getMatrix()).anyMatch(item -> item != null && this.keyManager.isKey(item))) {
            e.setCancelled(true);
        }
    }
}
