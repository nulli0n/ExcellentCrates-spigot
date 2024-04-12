package su.nightexpress.excellentcrates.api.opening;

import su.nightexpress.nightcore.util.random.Rnd;

public interface Weighted {

    default boolean checkRollChance() {
        return Rnd.chance(this.getRollChance());
    }

    double getWeight();

    void setWeight(double weight);

    double getRollChance();
}
