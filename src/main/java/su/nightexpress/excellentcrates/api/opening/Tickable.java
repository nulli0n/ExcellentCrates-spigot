package su.nightexpress.excellentcrates.api.opening;

public interface Tickable {

    void run();

    void stop();

    void emergencyStop();

    void tick();

    boolean isCompleted();

    long getInterval();

    long getTickCount();

    default boolean isTickTime() {
        return this.getTickCount() == 0 || this.getTickCount() % this.getInterval() == 0L;
    }

    boolean isRunning();
}
