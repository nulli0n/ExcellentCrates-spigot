package su.nightexpress.excellentcrates.animation.session;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;

@NullMarked
public class AnimationSessionService {

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

        ActionResult result = this.checkActiveSession(player);
        if (!result.success()) {
            return result;
        }

        AnimationInstance instance = profile.createInstance(context, onComplete);
        this.sessionManager.registerSession(player.getUniqueId(), instance);
        instance.start();

        return ActionResult.ok();
    }
}
