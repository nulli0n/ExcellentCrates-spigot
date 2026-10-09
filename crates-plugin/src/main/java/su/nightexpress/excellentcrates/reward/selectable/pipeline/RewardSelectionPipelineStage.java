package su.nightexpress.excellentcrates.reward.selectable.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ProcessCallback;
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
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;
import su.nightexpress.excellentcrates.crates.pipeline.DefaultPipelineReward;
import su.nightexpress.excellentcrates.reward.evaluation.pipeline.DefaultCrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickContext;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIController;

@NullMarked
public class RewardSelectionPipelineStage implements PipelineStage {

    private final SelectiveUIController uiController;

    public RewardSelectionPipelineStage(SelectiveUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        // Check if the crate uses selective rewards
        boolean isSelectiveRewards = crate.getComponent(CrateComponentKeys.SELECTABLE_REWARDS)
            .map(SelectableRewardsComponent::isEnabled).orElse(false);

        // If the crate does not use selective rewards, skip this stage.
        if (!isSelectiveRewards) {
            chain.proceed(player, context);
            return;
        }

        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) {
            chain.abort();
            return;
        }

        int batchSize = context.getComponent(PipelineComponentKeys.BATCH)
            .map(BatchPipelineComponent::getSelectedAmount).orElse(1);

        int requiredSelections = crateRewards.getRollCount() * batchSize;
        List<PipelineReward> targetRewards = new ArrayList<>();

        ProcessCallback<SelectivePickContext> callback = new ProcessCallback<SelectivePickContext>() {

            @Override
            public void cancel() {
                chain.abort();
            }

            @Override
            public void proceed(@Nullable SelectivePickContext result) {
                if (result == null || result.selectedRewards().size() != requiredSelections) {
                    chain.abort();
                    return;
                }

                result.selectedRewards().forEach(rewardRef -> {
                    Reward reward = rewardRef.get();
                    if (reward != null) {
                        targetRewards.add(new DefaultPipelineReward(reward, reward));
                    }
                });

                CrateRewardsPipelineComponent pipelineRewards = new DefaultCrateRewardsPipelineComponent();
                // We do not set filler rewards here, as they are not relevant for reward selection.
                pipelineRewards.setTargetRewards(targetRewards);
                pipelineRewards.setRequiredRewards(requiredSelections);

                context.putComponent(PipelineComponentKeys.REWARDS, pipelineRewards);

                chain.proceed(player, context);
            }
        };

        if (!this.uiController.startSelection(player, crate, requiredSelections, callback)) {
            chain.abort();
        }
    }
}
