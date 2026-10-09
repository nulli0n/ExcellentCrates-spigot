package su.nightexpress.excellentcrates.reward.evaluation.pipeline;

import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;
import su.nightexpress.excellentcrates.reward.evaluation.RollableReward;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;

@NullMarked
public class RewardValidationPipelineStage implements PipelineStage {

    private final RewardEvaluationService evaluationService;
    private final RewardMessageDispatcher dispatcher;

    public RewardValidationPipelineStage(RewardEvaluationService evaluationService,
                                         RewardMessageDispatcher dispatcher) {
        this.evaluationService = evaluationService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        // Check if there are any available rewards in the pool
        Set<RollableReward> pool = this.evaluationService.resolveAvailableRewards(crate, player);
        if (pool.isEmpty()) {
            this.dispatcher.sendBase(player, crate, RewardsLang.EVALUATION_NO_REWARDS);
            chain.abort();
            return;
        }

        context.getComponent(PipelineComponentKeys.BATCH).ifPresent(batch -> {
            boolean allLimited = pool.stream().allMatch(reward -> !reward.threshold().isAbsent());
            if (allLimited) {
                int rewardsRequired = crate.getComponent(CrateComponentKeys.REWARDS)
                    .map(CrateRewardsComponent::getRollCount)
                    .orElse(1);

                int totalRewardAmount = pool.stream()
                    .mapToInt(reward -> reward.threshold().getThreshold())
                    .sum();

                int maxBatch = totalRewardAmount / rewardsRequired;

                batch.limitMaxAllowed(maxBatch);
            }
        });

        chain.proceed(player, context);
    }

}
