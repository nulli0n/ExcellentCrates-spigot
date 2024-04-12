package su.nightexpress.excellentcrates.opening;

import su.nightexpress.excellentcrates.api.opening.Tickable;

public abstract class Runnable implements Tickable {

    protected long tickCount;
    protected boolean running;
    protected boolean emergency;

    public Runnable() {
        this.tickCount = 0L;
    }

    @Override
    public void run() {
        if (this.isRunning()) return;

        this.running = true;
        this.onLaunch();
    }

    @Override
    public void stop() {
        if (!this.isRunning()) return;

        this.running = false;
        this.onStop();
    }

    @Override
    public void emergencyStop() {
        this.emergency = true;
        this.stop();
    }

    @Override
    public void tick() {
        if (!this.isRunning()) return;

        if (this.isCompleted()) {
            this.stop();
            return;
        }

        if (this.isTickTime()) {
            this.onTick();
            this.tickCount = 0L;
        }
        this.tickCount++;
    }

    public boolean isEmergency() {
        return emergency;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public long getTickCount() {
        return this.tickCount;
    }

    protected abstract void onTick();

    protected abstract void onLaunch();

    protected abstract void onStop();
}
