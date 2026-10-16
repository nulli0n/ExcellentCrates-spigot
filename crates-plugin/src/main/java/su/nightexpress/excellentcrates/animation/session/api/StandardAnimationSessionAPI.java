package su.nightexpress.excellentcrates.animation.session.api;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.animation.session.AnimationSessionAPI;

@NullMarked
public class StandardAnimationSessionAPI implements AnimationSessionAPI {

    private final AnimationSessionService sessionService;

    public StandardAnimationSessionAPI(AnimationSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public ActionResult checkActiveSession(Player player) {
        return this.sessionService.checkActiveSession(player);
    }

    @Override
    public ActionResult startSession(AnimationProfile profile, AnimationContext context, Runnable onComplete) {
        return this.sessionService.startSession(profile, context, onComplete);
    }
}
