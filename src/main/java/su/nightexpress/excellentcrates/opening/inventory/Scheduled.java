package su.nightexpress.excellentcrates.opening.inventory;

import org.jetbrains.annotations.NotNull;

public class Scheduled {

    private final Runnable runnable;

    private long ticksLeft;
    private boolean completed;

    public Scheduled(@NotNull Runnable runnable, long ticksLeft) {
        this.runnable = runnable;
        this.ticksLeft = ticksLeft;
    }

    public void forceRun() {
        if (this.isCompleted()) return;

        this.ticksLeft = 1L;
        this.tick();
    }

    public void tick() {
        if (this.isCompleted()) return;

        this.ticksLeft--;

        if (this.isReady()) {
            this.runnable.run();
            this.completed = true;
        }
    }

    public boolean isReady() {
        return this.ticksLeft <= 0L;
    }

    public boolean isCompleted() {
        return completed;
    }
}
