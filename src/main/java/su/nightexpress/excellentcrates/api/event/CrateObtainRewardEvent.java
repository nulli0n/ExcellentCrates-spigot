package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Reward;

public class CrateObtainRewardEvent extends CrateEvent {

    private final Reward reward;

    public CrateObtainRewardEvent(@NotNull Reward reward, @NotNull Player player) {
        super(reward.getCrate(), player);
        this.reward = reward;
    }

    @NotNull
    public Reward getReward() {
        return reward;
    }
}
