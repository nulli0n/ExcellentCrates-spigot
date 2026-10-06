package su.nightexpress.excellentcrates.reward.evaluation.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;
import su.nightexpress.excellentcrates.crates.pipeline.DefaultPipelineReward;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;
import su.nightexpress.excellentcrates.reward.evaluation.RollableReward;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.excellentcrates.util.WeightedRandomSelector;
import su.nightexpress.nightcore.util.Randomizer;

@NullMarked
public class RewardEvaluationPipelineStage implements PipelineStage {

    private final RewardEvaluationService evaluationService;
    private final RewardMessageDispatcher dispatcher;

    public RewardEvaluationPipelineStage(RewardEvaluationService evaluationService,
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

        // Check if the crate uses selective rewards
        boolean isSelectiveRewards = crate.getComponent(CrateComponentKeys.SELECTABLE_REWARDS)
            .map(SelectableRewardsComponent::isEnabled).orElse(false);

        // If so, proceed with the selective rewards pipeline and skip the evaluation stage.
        if (isSelectiveRewards) {
            chain.proceed(player, context);
            return;
        }

        CrateRewardsPipelineComponent crateRewards = new DefaultCrateRewardsPipelineComponent();

        // Get the batch size
        int batchSize = context.getComponent(PipelineComponentKeys.BATCH)
            .map(BatchPipelineComponent::getSelectedAmount).orElse(1);

        int requiredRewards = this.evaluationService.getRequiredRewards(crate) * batchSize;
        List<PipelineReward> rolledRewards = new ArrayList<>(requiredRewards);
        List<List<Reward>> fillerRewards = new ArrayList<>(requiredRewards);

        WeightedRandomSelector selector = new WeightedRandomSelector(Randomizer.getSource());

        for (int index = 0; index < requiredRewards; index++) {
            if (pool.isEmpty()) {
                // Break the loop as there are no more available rewards to roll.
                break;
            }

            List<Reward> currentFillerPool = pool.stream()
                .filter(RollableReward::isAvailable)
                .map(RollableReward::reward)
                .toList();

            fillerRewards.add(currentFillerPool);

            // Select the target reward from the pool based on their weights
            RollableReward target = selector.selectItem(pool, RollableReward::weight);

            // Increment the roll count for the selected reward
            target.incrementRolled();

            rolledRewards.add(new DefaultPipelineReward(target.reward(), target.reward()));

            // If the reward's limit is reached, remove it from the pool for subsequent rolls
            if (!target.isAvailable()) {
                pool.remove(target);
            }
        }

        crateRewards.setRequiredRewards(requiredRewards);
        crateRewards.setFillerRewards(fillerRewards);
        crateRewards.setTargetRewards(rolledRewards);

        context.putComponent(PipelineComponentKeys.REWARDS, crateRewards);
        chain.proceed(player, context);
    }
}
