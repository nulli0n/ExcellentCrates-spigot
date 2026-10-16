package su.nightexpress.excellentcrates.animation.session;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;

@NullMarked
public class AnimationSessionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationSessionController.class);

    private final AnimationSessionManager sessionManager;

    public AnimationSessionController(CratesPlugin plugin, AnimationSessionManager sessionManager) {
        super(plugin);
        this.sessionManager = sessionManager;
    }

    @Override
    protected void onControllerReload() {

    }

    @Override
    protected void onControllerShutdown() {
        this.sessionManager.setShuttingDown(true);
        this.stopAllSessions();
    }

    @Override
    protected void onControllerStart() {
        this.sessionManager.setShuttingDown(false);
        this.runInstanceTickTask();
    }

    private void runInstanceTickTask() {
        this.addTickTask(this::tickSessions, 1);
    }

    private void tickSessions() {
        this.sessionManager.purgeStoppedSessions();

        for (Map.Entry<UUID, AnimationInstance> entry : this.sessionManager.getSessionEntries()) {
            AnimationInstance session = entry.getValue();
            if (!session.isRunning()) continue;

            try {
                if (!session.tick() && session.isRunning()) {
                    session.stop();
                }
            }
            catch (RuntimeException exception) {
                LOGGER.error("Failed to tick an opening animation", exception);
                if (session.isRunning()) {
                    this.stopSession(entry.getKey(), session);
                }
            }
        }
    }

    private void stopAllSessions() {
        for (Map.Entry<UUID, AnimationInstance> entry : this.sessionManager.getSessionEntries()) {
            this.stopSession(entry.getKey(), entry.getValue());
        }

        this.sessionManager.purgeStoppedSessions();
    }

    private void stopSession(UUID playerId, AnimationInstance session) {
        try {
            this.sessionManager.stopSession(playerId, session);
        }
        catch (RuntimeException exception) {
            LOGGER.error("Failed to stop an opening animation", exception);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        this.sessionManager.stopSession(playerId);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        this.sessionManager.stopSession(playerId);
    }
}
