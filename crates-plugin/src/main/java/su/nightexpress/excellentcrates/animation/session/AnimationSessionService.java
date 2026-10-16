package su.nightexpress.excellentcrates.animation.session;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;

@NullMarked
public class AnimationSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationSessionService.class);

    private final AnimationSessionManager sessionManager;

    public AnimationSessionService(AnimationSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ActionResult checkActiveSession(Player player) {
        if (!this.sessionManager.canStartSession(player.getUniqueId()) || this.sessionManager.hasSession(player.getUniqueId())) {
            return ActionResult.fail(AnimationsLang.GENERIC_ANIMATION_SESSION_ACTIVE);
        }
        return ActionResult.ok();
    }

    public ActionResult startSession(AnimationProfile profile, AnimationContext context, Runnable onComplete) {
        Player player = context.player();

        ActionResult result = this.checkActiveSession(player);
        if (!result.success()) {
            return result;
        }

        AtomicBoolean completed = new AtomicBoolean();
        AtomicBoolean failed = new AtomicBoolean();
        @Nullable AnimationInstance instance = null;
        try {
            instance = profile.createInstance(context, () -> {
                if (!failed.get()) {
                    completed.set(true);
                    onComplete.run();
                }
            });
            this.sessionManager.registerSession(player.getUniqueId(), instance);
            instance.start();
        }
        catch (RuntimeException exception) {
            // Suppress callbacks only while cleaning up a failed startup.
            failed.set(true);
            if (instance != null && instance.isRunning()) {
                if (this.sessionManager.getSession(player.getUniqueId()) == instance) {
                    this.sessionManager.unregisterSession(player.getUniqueId());
                }
                try {
                    instance.stop();
                }
                catch (RuntimeException cleanupException) {
                    exception.addSuppressed(cleanupException);
                }
            }
            LOGGER.error("Failed to start animation '{}'", profile.getKey(), exception);
            // A synchronous completion may already have advanced the chain.
            return completed.get() ? ActionResult.ok() : ActionResult.fail(AnimationsLang.PIPELINE_INVALID_ANIMATION_INSTANTIATOR);
        }

        return ActionResult.ok();
    }
}
