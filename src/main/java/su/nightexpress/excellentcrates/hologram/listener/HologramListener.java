package su.nightexpress.excellentcrates.hologram.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.hologram.HologramManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class HologramListener extends AbstractListener<CratesPlugin> {

    private final HologramManager manager;

    public HologramListener(@NotNull CratesPlugin plugin, @NotNull HologramManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        this.manager.handleQuit(event.getPlayer());
    }
}
