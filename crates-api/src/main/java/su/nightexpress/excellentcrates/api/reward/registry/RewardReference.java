package su.nightexpress.excellentcrates.api.reward.registry;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardReference {

    RewardId id();

    @Nullable
    Reward get();

    default Reward require() {
        Reward reward = this.get();
        if (reward == null) throw new IllegalStateException("Reward '" + this.id() + "' no longer exists.");
        return reward;
    }
}
