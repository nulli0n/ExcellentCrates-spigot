package su.nightexpress.excellentcrates.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;

public class CrateOpenEvent extends CrateEvent implements Cancellable {

    private boolean isCancelled;

    public CrateOpenEvent(@NotNull Crate crate, @NotNull Player player) {
        super(crate, player);
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}
