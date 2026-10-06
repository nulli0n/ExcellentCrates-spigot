package su.nightexpress.excellentcrates.reward.feature.limit.pipeline;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.limit.LimitRemaining;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.reward.feature.limit.RewardLimitManageService;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;

@NullMarked
public class RewardLimitsAlternativePipelineStage implements PipelineStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardLimitsAlternativePipelineStage.class);

    private final RewardResolver           resolver;
    private final RewardLimitManageService limitService;
    private final RewardMessageDispatcher  dispatcher;

    public RewardLimitsAlternativePipelineStage(RewardResolver resolver,
                                                RewardLimitManageService limitService,
                                                RewardMessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.limitService = limitService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        CrateRewardsPipelineComponent rewardsComponent = context.getComponentOrNull(
            PipelineComponentKeys.REWARDS
        );

        if (rewardsComponent == null) {
            chain.proceed(player, context);
            return;
        }

        List<PipelineReward> rewards = rewardsComponent.getTargetRewards();

        for (PipelineReward reward : rewards) {
            Reward rolled = reward.getRolled();

            LimitRemaining remaining = this.limitService.getRemainingRolls(player, rolled);
            if (remaining.isExhausted()) {
                if (!this.handleAlternativeReward(reward, rolled)) {
                    this.dispatcher.sendBase(player, rolled, RewardLimitsLang.ALTERNATIVE_REWARD_EVALUATION_FAILED);
                    chain.abort();
                    break;
                }
            }
        }

        chain.proceed(player, context);
    }

    private boolean handleAlternativeReward(PipelineReward reward, Reward rolled) {
        RewardLimitComponent limit = rolled.getComponentOrNull(RewardComponentKeys.LIMIT);
        if (limit == null || !limit.isAlternativeEnabled()) return false;

        Identifier altId = limit.getAlternativeRewardId();
        Reward alternative = this.resolver.resolveReward(altId);
        if (alternative == null) {
            LOGGER.warn("Alternative reward with ID '{}' not found for reward '{}'", altId, rolled.getId());
            return false;
        }

        reward.setGranted(alternative);
        return true;
    }
}
