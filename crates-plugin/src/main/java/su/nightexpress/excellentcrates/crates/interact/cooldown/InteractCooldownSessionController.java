package su.nightexpress.excellentcrates.crates.interact.cooldown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;

@NullMarked
public class InteractCooldownSessionController extends BaseController {

    private final InteractCooldownService cooldownService;

    public InteractCooldownSessionController(CratesPlugin plugin, InteractCooldownService cooldownService) {
        super(plugin);
        this.cooldownService = cooldownService;
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
        this.cooldownService.clearAllCooldowns(event.getPlayer());
    }
}
