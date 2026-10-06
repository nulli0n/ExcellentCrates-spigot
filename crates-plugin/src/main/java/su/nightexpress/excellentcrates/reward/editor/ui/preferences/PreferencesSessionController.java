package su.nightexpress.excellentcrates.reward.editor.ui.preferences;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.nightcore.NightCorePlugin;

@NullMarked
public class PreferencesSessionController extends BaseController {

    private final PreferencesSessionManager sessionManager;

    public PreferencesSessionController(NightCorePlugin plugin, PreferencesSessionManager sessionManager) {
        super(plugin);
        this.sessionManager = sessionManager;
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.sessionManager.clearSession(event.getPlayer().getUniqueId());
    }
}
