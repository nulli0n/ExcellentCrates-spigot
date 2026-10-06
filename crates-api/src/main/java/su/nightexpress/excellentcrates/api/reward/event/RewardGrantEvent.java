package su.nightexpress.excellentcrates.api.reward.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public class RewardGrantEvent extends Event implements Cancellable {

    public static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Crate  crate;
    private final Reward reward;

    private boolean cancelled;

    public RewardGrantEvent(Player player, Crate crate, Reward reward) {
        super();
        this.player = player;
        this.crate = crate;
        this.reward = reward;
    }

    public Player getPlayer() {
        return player;
    }

    public Crate getCrate() {
        return crate;
    }

    public Reward getReward() {
        return reward;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
