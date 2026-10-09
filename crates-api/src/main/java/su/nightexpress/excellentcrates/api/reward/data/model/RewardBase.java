package su.nightexpress.excellentcrates.api.reward.data.model;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RewardBase {

    double getWeight();

    void setWeight(double weight);
}
