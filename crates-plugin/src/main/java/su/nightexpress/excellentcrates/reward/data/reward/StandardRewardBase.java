package su.nightexpress.excellentcrates.reward.data.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.data.model.RewardBase;

@NullMarked
public class StandardRewardBase implements RewardBase {

    private double weight;

    public StandardRewardBase(double weight) {
        this.weight = weight;
    }

    public static StandardRewardBase createDefault() {
        return new StandardRewardBase(0D);
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
