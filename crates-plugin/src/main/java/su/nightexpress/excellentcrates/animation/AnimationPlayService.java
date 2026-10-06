package su.nightexpress.excellentcrates.animation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.AnimationProfileComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateSourcePipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;

@NullMarked
public class AnimationPlayService {

    private final AnimationRegistry       registry;
    private final AnimationSessionService sessionService;
    private final RewardPreviewAPI        rewardPreview;

    public AnimationPlayService(AnimationRegistry registry,
                                AnimationSessionService sessionService,
                                RewardPreviewAPI rewardPreview) {
        this.registry = registry;
        this.sessionService = sessionService;
        this.rewardPreview = rewardPreview;
    }

    public ActionResult playAnimation(Player player, PipelineContext context, Runnable onComplete) {
        ActionResult sessionCheck = this.sessionService.checkActiveSession(player);
        if (!sessionCheck.success()) {
            return sessionCheck;
        }

        Crate crate = context.getCrate();

        AnimationProfileComponent pipelineAnimation = context.getComponentOrNull(
            PipelineComponentKeys.ANIMATION
        );
        if (pipelineAnimation == null) {
            onComplete.run();
            return ActionResult.fail(AnimationsLang.PIPELINE_NO_ANIMATION_COMPONENT);
        }

        AdaptedKey profileKey = pipelineAnimation.getProfileKey();
        AnimationProfile profile = this.registry.getProfile(profileKey);
        if (profile == null) {
            onComplete.run();
            return ActionResult.fail(AnimationsLang.PIPELINE_INVALID_ANIMATION_INSTANTIATOR);
        }

        CrateRewardsPipelineComponent crateRewards = context.getComponentOrNull(
            PipelineComponentKeys.REWARDS
        );
        if (crateRewards == null) {
            onComplete.run();
            return ActionResult.fail(AnimationsLang.PIPELINE_NO_REWARDS_COMPONENT);
        }

        CrateSourcePipelineComponent pipelineSource = context.getComponentOrNull(
            PipelineComponentKeys.CRATE_SOURCE
        );

        Location sourceLocation = pipelineSource == null ? null : pipelineSource.getLocation();
        Location location;
        if (sourceLocation == null) {
            double eyeHeight = player.getEyeLocation().getY();
            Location targetLocation = player.getLocation();
            BlockFace direction = EntityUtil.getDirection(player);
            if (direction == null) {
                direction = BlockFace.SELF;
            }

            Location nextLocation = targetLocation.getBlock().getRelative(direction).getLocation().clone();
            location = LocationUtil.setCenter2D(nextLocation);
            location.setY(eyeHeight);
        }
        else {
            location = sourceLocation.clone();
        }

        List<PipelineReward> targetRewards = List.copyOf(crateRewards.getTargetRewards());
        List<List<Reward>> fillerRewards = List.copyOf(crateRewards.getFillerRewards());

        AnimationChainRequest request = new AnimationChainRequest(
            profile,
            player,
            location,
            crate,
            targetRewards,
            fillerRewards,
            onComplete
        );

        return this.playSequential(request, 0);
    }

    private ActionResult playSequential(AnimationChainRequest request, int currentIndex) {
        // All rewards have been played
        if (currentIndex >= request.targetRewards().size()) {
            request.finalComplete().run();
            return ActionResult.ok();
        }

        // Isolate the context for a single specific animation.
        PipelineReward reward = request.targetRewards().get(currentIndex);
        List<Reward> fillerRewards = List.copyOf(request.fillerRewards().get(currentIndex));

        AnimationContext animationContext = new AnimationContext(
            request.player(),
            request.location(),
            request.crate(),
            reward,
            fillerRewards,
            this.rewardPreview
        );

        // Create a closure (chain)
        Runnable onSessionEnd = () -> {
            this.playSequential(request, currentIndex + 1);
        };

        // Attempt to start the session
        ActionResult result = this.sessionService.startSession(request.profile(), animationContext, onSessionEnd);

        // If starting the session failed, force complete the pipeline.
        if (!result.success()) {
            request.finalComplete().run();
        }

        return result;
    }

    private record AnimationChainRequest(AnimationProfile profile,
                                         Player player,
                                         Location location,
                                         Crate crate,
                                         List<PipelineReward> targetRewards,
                                         List<List<Reward>> fillerRewards,
                                         Runnable finalComplete) {
    }
}
