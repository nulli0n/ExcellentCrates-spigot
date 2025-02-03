package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.Reward;

public class CrateObtainRewardEvent extends CrateEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final Reward reward;

    public CrateObtainRewardEvent(@NotNull Reward reward, @NotNull Player player) {
        super(reward.getCrate(), player);
        this.reward = reward;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @NotNull
    public Reward getReward() {
        return reward;
    }
}
