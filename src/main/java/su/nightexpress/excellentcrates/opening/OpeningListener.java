package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.api.event.PlayerOpenMenuEvent;
import su.nightexpress.nightcore.manager.AbstractListener;

public class OpeningListener extends AbstractListener<CratesPlugin> {

    private final OpeningManager openingManager;

    public OpeningListener(@NotNull CratesPlugin plugin, @NotNull OpeningManager openingManager) {
        super(plugin);
        this.openingManager = openingManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        this.openingManager.stopOpening(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuOpen(PlayerOpenMenuEvent event) {
        Player player = event.getPlayer();
        if (!this.openingManager.isOpening(player)) return;

        Opening opening = this.openingManager.getOpeningData(player);

        if (opening instanceof InventoryOpening inventoryOpening) {
            if (event.getMenu() != inventoryOpening.getMenu()) {
                event.setCancelled(true);
            }
            return;
        }

        event.setCancelled(true);
    }
}
