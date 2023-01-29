package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.CrateReward;

public class CrateObtainRewardEvent extends CrateEvent {

    private final CrateReward reward;

    public CrateObtainRewardEvent(@NotNull CrateReward reward, @NotNull Player player) {
        super(reward.getCrate(), player);
        this.reward = reward;
    }

    @NotNull
    public CrateReward getReward() {
        return reward;
    }
}
