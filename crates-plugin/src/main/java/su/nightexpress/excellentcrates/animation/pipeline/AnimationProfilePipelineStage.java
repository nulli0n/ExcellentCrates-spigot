package su.nightexpress.excellentcrates.animation.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class AnimationProfilePipelineStage implements PipelineStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationProfilePipelineStage.class);

    private final AnimationRegistry       registry;
    private final AnimationSessionService sessionService;
    private final CrateMessageDispatcher  dispatcher;

    public AnimationProfilePipelineStage(AnimationRegistry registry,
                                         AnimationSessionService sessionService,
                                         CrateMessageDispatcher dispatcher) {
        this.registry = registry;
        this.sessionService = sessionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        ActionResult sessionCheck = this.sessionService.checkActiveSession(player);
        if (!sessionCheck.success()) {
            sessionCheck.handleFeedback((locale, ctx) -> this.dispatcher.sendBase(player, crate, locale, ctx));
            chain.abort();
            return;
        }

        // Check if the crate should be opened quickly without animation.
        boolean fastOpen = context.hasComponent(PipelineComponentKeys.FAST_OPEN);
        if (fastOpen) {
            chain.proceed(player, context);
            return;
        }

        AnimationComponent crateAnimation = crate.getComponentOrNull(CrateComponentKeys.ANIMATION);
        if (crateAnimation == null || !crateAnimation.isEnabled()) {
            chain.proceed(player, context);
            return;
        }

        AdaptedKey profileKey = crateAnimation.getProfileKey();
        AnimationProfile config = this.registry.getProfile(profileKey);
        if (config == null) {
            LOGGER.warn("No animation profile found for key '{}' in crate '{}'", profileKey, crate.id());
            chain.proceed(player, context);
            return;
        }

        context.putComponent(PipelineComponentKeys.ANIMATION, new DefaultAnimationProfileComponent(profileKey));

        chain.proceed(player, context);
    }
}
