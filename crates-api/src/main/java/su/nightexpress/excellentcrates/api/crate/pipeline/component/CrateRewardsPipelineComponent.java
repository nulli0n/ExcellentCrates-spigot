package su.nightexpress.excellentcrates.api.crate.pipeline.component;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface CrateRewardsPipelineComponent extends LoggablePipelineComponent {

    int getRequiredRewards();

    void setRequiredRewards(int requiredRewards);

    List<PipelineReward> getTargetRewards();

    void setTargetRewards(List<PipelineReward> rewards);

    List<List<Reward>> getFillerRewards();

    void setFillerRewards(List<List<Reward>> rewards);
}
