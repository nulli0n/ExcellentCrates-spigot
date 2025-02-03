package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;

public abstract class CrateEvent extends Event {

    private final Crate crate;
    private final Player player;

    public CrateEvent(@NotNull Crate crate, @NotNull Player player) {
        this.crate = crate;
        this.player = player;
    }

    @NotNull
    public Crate getCrate() {
        return this.crate;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }
}
