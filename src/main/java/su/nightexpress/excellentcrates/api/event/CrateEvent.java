package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.ICrate;

public abstract class CrateEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private final ICrate crate;
	private final Player player;
	
	public CrateEvent(@NotNull ICrate crate, @NotNull Player player) {
		this.crate = crate;
		this.player = player;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	@NotNull
	public ICrate getCrate() {
		return this.crate;
	}
	
	@NotNull
	public Player getPlayer() {
		return this.player;
	}
}
