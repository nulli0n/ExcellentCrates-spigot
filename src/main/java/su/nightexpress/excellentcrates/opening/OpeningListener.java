package su.nightexpress.excellentcrates.opening;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.manager.AbstractListener;

public class OpeningListener extends AbstractListener<CratesPlugin> {

    private final OpeningManager manager;

    public OpeningListener(@NotNull CratesPlugin plugin, @NotNull OpeningManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        this.manager.stopOpening(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInvOpeningClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Opening opening = this.manager.getOpening(player);
        if (!(opening instanceof InventoryOpening inventoryOpening)) return;

        inventoryOpening.onClick(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInvOpeningClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Opening opening = this.manager.getOpening(player);
        if (!(opening instanceof InventoryOpening inventoryOpening)) return;

        if (inventoryOpening.isLaunched() && !opening.isCompleted()) {
            if (inventoryOpening.canSkip()) {
                opening.instaRoll();
            }
//            else  {
//                inventoryOpening.setPopupNextTick(true);
//            }
        }
        else {
            inventoryOpening.setCloseTicks(0);
            opening.stop();
        }

        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInvOpeningOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Opening opening = this.manager.getOpening(player);
        if (!(opening instanceof InventoryOpening inventoryOpening)) return;
        if (!inventoryOpening.isLaunched()) return;

        if (inventoryOpening.getView() != event.getView()) {
            event.setCancelled(true);
        }
    }
}
