package su.nightexpress.excellentcrates.api.crate.pipeline.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface PipelineReward {

    Reward getRolled();

    Reward getGranted();

    void setGranted(Reward granted);
}
