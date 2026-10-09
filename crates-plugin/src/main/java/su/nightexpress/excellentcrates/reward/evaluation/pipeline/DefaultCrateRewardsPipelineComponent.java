package su.nightexpress.excellentcrates.reward.evaluation.pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public class DefaultCrateRewardsPipelineComponent implements CrateRewardsPipelineComponent {

    private int                  requiredRewards;
    private List<PipelineReward> targetRewards;
    private List<List<Reward>>   fillerRewards;

    public DefaultCrateRewardsPipelineComponent() {
        this.requiredRewards = 1;
        this.targetRewards = new ArrayList<>();
        this.fillerRewards = new ArrayList<>();
    }

    @Override
    public Map<String, String> getLogData() {
        Map<String, String> logData = new HashMap<>();

        logData.put("Rewards Required", String.valueOf(this.requiredRewards));
        logData.put("Rewards Given", this.targetRewards.stream()
            .map(pipeReward -> pipeReward.getGranted().id().toString())
            .collect(Collectors.joining(", "))
        );

        return logData;
    }

    @Override
    public int getRequiredRewards() {
        return this.requiredRewards;
    }

    @Override
    public void setRequiredRewards(int requiredRewards) {
        this.requiredRewards = requiredRewards;
    }

    @Override
    public List<PipelineReward> getTargetRewards() {
        return this.targetRewards;
    }

    @Override
    public void setTargetRewards(List<PipelineReward> rewards) {
        this.targetRewards = rewards;
    }

    @Override
    public List<List<Reward>> getFillerRewards() {
        return this.fillerRewards;
    }

    @Override
    public void setFillerRewards(List<List<Reward>> availableRewards) {
        this.fillerRewards = availableRewards;
    }

}
