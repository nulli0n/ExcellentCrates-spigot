package su.nightexpress.excellentcrates.animation.session;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
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
        if (this.sessionManager.hasSession(player.getUniqueId())) {
            return ActionResult.fail(AnimationsLang.GENERIC_ANIMATION_SESSION_ACTIVE);
        }
        return ActionResult.ok();
    }

    public ActionResult startSession(AnimationProfile profile, AnimationContext context, Runnable onComplete) {
        Player player = context.player();

        if (!this.sessionManager.canStartSession(player.getUniqueId())) {
            return ActionResult.fail(AnimationsLang.GENERIC_ANIMATION_SESSION_ACTIVE);
        }

        ActionResult result = this.checkActiveSession(player);
        if (!result.success()) {
            return result;
        }

        AtomicBoolean completed = new AtomicBoolean();
        try {
            AnimationInstance instance = profile.createInstance(context, () -> {
                if (completed.compareAndSet(false, true)) {
                    onComplete.run();
                }
            });
            this.sessionManager.registerSession(player.getUniqueId(), instance);
            instance.start();
        }
        catch (RuntimeException exception) {
            // The caller completes a failed startup. Cleanup must not also advance the chain.
            completed.set(true);
            try {
                this.sessionManager.stopSession(player.getUniqueId());
            }
            catch (RuntimeException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            LOGGER.error("Failed to start animation '{}'", profile.getKey(), exception);
            return ActionResult.fail(AnimationsLang.PIPELINE_INVALID_ANIMATION_INSTANTIATOR);
        }

        return ActionResult.ok();
    }
}
