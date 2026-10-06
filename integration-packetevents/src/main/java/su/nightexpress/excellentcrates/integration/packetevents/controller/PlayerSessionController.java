package su.nightexpress.excellentcrates.integration.packetevents.controller;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.integration.packetevents.PacketEventsHologramProvider;
import su.nightexpress.nightcore.NightCorePlugin;

@NullMarked
public class PlayerSessionController extends BaseController {

    private final PacketEventsHologramProvider provider;

    public PlayerSessionController(NightCorePlugin plugin, PacketEventsHologramProvider provider) {
        super(plugin);
        this.provider = provider;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.provider.removeForViewer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        this.provider.removeForViewer(event.getPlayer());
    }
}
