package su.nightexpress.excellentcrates.api.opening;

import org.jetbrains.annotations.NotNull;

public interface Spinner {

    void start();

    void stop();

    void tick();

    void tickAll();

    boolean isRunning();

    boolean isCompleted();

    long getTickInterval();

    long getTickCount();

    boolean isSpinTime();

    @NotNull String getId();

    boolean isSilent();

    void setSilent(boolean silent);

    int getTotalSpins();

    long getStepCount();

    void setStepCount(long spins);

    boolean hasSpin();
}
