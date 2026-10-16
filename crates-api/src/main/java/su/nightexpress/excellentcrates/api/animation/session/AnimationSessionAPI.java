package su.nightexpress.excellentcrates.api.animation.session;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;

@NullMarked
public interface AnimationSessionAPI {

    ActionResult checkActiveSession(Player player);

    ActionResult startSession(AnimationProfile profile, AnimationContext context, Runnable onComplete);
}
