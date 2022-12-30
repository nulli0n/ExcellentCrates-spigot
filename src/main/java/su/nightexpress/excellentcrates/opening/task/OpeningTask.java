package su.nightexpress.excellentcrates.opening.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;

public abstract class OpeningTask extends BukkitRunnable {

    protected final PlayerOpeningData data;
    protected boolean isStarted;

    public OpeningTask(@NotNull PlayerOpeningData data) {
        this.data = data;
    }

    public abstract boolean canSkip();

    public final boolean start() {
        if (this.isStarted()) return false;

        this.isStarted = this.onStart();
        return this.isStarted;
    }

    protected abstract boolean onStart();

    public final boolean stop(boolean force) {
        if (this.isStarted() && this.isCancelled()) return false;

        if (this.onStop(force)) {
            this.cancel();
            return true;
        }
        return false;
    }

    protected abstract boolean onStop(boolean force);

    @NotNull
    public PlayerOpeningData getData() {
        return data;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
