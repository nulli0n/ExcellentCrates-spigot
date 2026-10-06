package su.nightexpress.excellentcrates.reward.evaluation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public class RollableReward {

    private final Reward         reward;
    private final double         initialWeight;
    private final QuotaThreshold threshold;

    private int timesRolled;

    public RollableReward(Reward reward, double initialWeight, QuotaThreshold threshold) {
        this.reward = reward;
        this.initialWeight = initialWeight;
        this.threshold = threshold;
    }

    public Reward reward() {
        return this.reward;
    }

    public double weight() {
        return this.initialWeight;
    }

    public QuotaThreshold threshold() {
        return this.threshold;
    }

    public void incrementRolled() {
        this.timesRolled++;
    }

    /**
     * @return true, если награду можно выбросить еще раз.
     */
    public boolean isAvailable() {
        return this.threshold.isAbsent() || this.timesRolled < this.threshold.getThreshold();
    }
}