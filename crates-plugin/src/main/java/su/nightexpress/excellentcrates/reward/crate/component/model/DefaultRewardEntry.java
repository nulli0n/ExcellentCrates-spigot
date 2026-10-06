package su.nightexpress.excellentcrates.reward.crate.component.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;

@NullMarked
public class DefaultRewardEntry implements CrateRewardEntry {

    private Identifier rewardId;
    private double     weight;

    public DefaultRewardEntry(Identifier rewardId, double weight) {
        this.rewardId = rewardId;
        this.weight = weight;
    }

    @Override
    public Identifier getRewardId() {
        return rewardId;
    }

    @Override
    public void setRewardId(Identifier rewardId) {
        this.rewardId = rewardId;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
