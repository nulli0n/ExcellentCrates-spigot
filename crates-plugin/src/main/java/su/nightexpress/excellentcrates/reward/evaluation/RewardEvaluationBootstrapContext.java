package su.nightexpress.excellentcrates.reward.evaluation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.evaluation.pipeline.RewardEvaluationPipelineStage;
import su.nightexpress.excellentcrates.reward.evaluation.pipeline.RewardValidationPipelineStage;
import su.nightexpress.excellentcrates.reward.evaluation.placeholder.CrateTotalWeightPlaceholder;
import su.nightexpress.excellentcrates.reward.evaluation.placeholder.RewardEvaluationPlaceholder;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;

@NullMarked
public class RewardEvaluationBootstrapContext {

    public final RewardEvaluationService evaluationService;

    private final CrateTotalWeightPlaceholder   cratePlaceholder;
    private final RewardPlaceholder             rewardPlaceholder;
    private final RewardValidationPipelineStage validationPipelineStage;
    private final RewardEvaluationPipelineStage evaluationPipelineStage;

    public RewardEvaluationBootstrapContext(RewardRegistry rewards,
                                            RewardMessageDispatcher dispatcher,
                                            RewardQuotaService quotaService) {
        this.evaluationService = new RewardEvaluationService(rewards, quotaService);

        this.cratePlaceholder = new CrateTotalWeightPlaceholder(this.evaluationService);
        this.rewardPlaceholder = new RewardEvaluationPlaceholder(this.evaluationService);

        this.validationPipelineStage = new RewardValidationPipelineStage(this.evaluationService, dispatcher);
        this.evaluationPipelineStage = new RewardEvaluationPipelineStage(this.evaluationService, dispatcher);
    }

    public PipelineStage getValidationPipelineStage() {
        return this.validationPipelineStage;
    }

    public PipelineStage getEvaluationPipelineStage() {
        return this.evaluationPipelineStage;
    }

    public RewardPlaceholder getRewardPlaceholder() {
        return this.rewardPlaceholder;
    }

    public CrateTotalWeightPlaceholder getCratePlaceholder() {
        return this.cratePlaceholder;
    }
}
