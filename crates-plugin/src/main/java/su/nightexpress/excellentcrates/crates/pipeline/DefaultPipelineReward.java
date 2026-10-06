package su.nightexpress.excellentcrates.crates.pipeline;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public class DefaultPipelineReward implements PipelineReward {

    private final Reward rolled;
    private Reward       granted;

    public DefaultPipelineReward(Reward rolled, Reward granted) {
        this.rolled = rolled;
        this.granted = granted;
    }

    @Override
    public Reward getRolled() {
        return rolled;
    }

    @Override
    public Reward getGranted() {
        return granted;
    }

    @Override
    public void setGranted(Reward granted) {
        this.granted = granted;
    }
}
