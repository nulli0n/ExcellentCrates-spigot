package su.nightexpress.excellentcrates.opening;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
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
}
