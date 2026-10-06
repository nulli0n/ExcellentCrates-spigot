package su.nightexpress.excellentcrates.animation.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.animation.AnimationPlayService;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineProcessor;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.AnimationProfileComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class AnimationPipelineProcessor implements PipelineProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationPipelineProcessor.class);

    private final AnimationRegistry      animationRegistry;
    private final AnimationPlayService   playService;
    private final CrateMessageDispatcher dispatcher;

    public AnimationPipelineProcessor(AnimationRegistry animationRegistry,
                                      AnimationPlayService playService,
                                      CrateMessageDispatcher dispatcher) {
        this.animationRegistry = animationRegistry;
        this.playService = playService;
        this.dispatcher = dispatcher;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean shouldHandle(Crate crate, PipelineContext context) {
        // Do not handle if the animation component is not present or not enabled
        AnimationComponent crateAnimation = crate.getComponentOrNull(CrateComponentKeys.ANIMATION);
        if (crateAnimation == null || !crateAnimation.isEnabled()) return false;

        // Do not handle if there is no animation configuration set in the pipeline context
        AnimationProfileComponent pipelineAnimation = context.getComponentOrNull(PipelineComponentKeys.ANIMATION);
        if (pipelineAnimation == null) return false;

        AdaptedKey animationKey = crateAnimation.getProfileKey();
        if (this.animationRegistry.getProfile(animationKey) == null) {
            LOGGER.warn("No instantiator found for animation key '{}' in crate '{}'", animationKey, crate.id());
            return false;
        }

        // Do not handle if there are no filler rewards
        CrateRewardsPipelineComponent rewards = context.getComponentOrNull(PipelineComponentKeys.REWARDS);
        return rewards != null && !rewards.getFillerRewards().isEmpty();
    }

    @Override
    public void process(Player player, PipelineContext context, Runnable onComplete) {
        Crate crate = context.getCrate();

        this.dispatcher.handleFeedbackBase(player, crate, this.playService.playAnimation(player, context, onComplete));
    }
}
