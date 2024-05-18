package su.nightexpress.excellentcrates.opening;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.opening.spinner.SpinSettings;
import su.nightexpress.excellentcrates.api.opening.Spinner;

public abstract class AbstractSpinner extends Runnable implements Spinner {

    protected final CratesPlugin plugin;
    protected final String       id;

    protected boolean silent;
    protected long spinCount;
    protected long spinSpeedTicks;

    public AbstractSpinner(@NotNull CratesPlugin plugin, @NotNull String id) {
        this.plugin = plugin;
        this.id = id.toLowerCase();
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isSilent() {
        return silent;
    }

    @Override
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @NotNull
    protected abstract Opening getOpening();

    @NotNull
    protected abstract SpinSettings getSettings();

    protected abstract void onSpin();

    @Override
    protected void onLaunch() {
        this.spinSpeedTicks = this.getSettings().getSpinTickInterval();
    }

    @Override
    public void onTick() {
        //System.out.println("SpSd/TiCt/SpCt: " + this.spinSpeedTicks + " / " + this.tickCount + " / " + this.spinCount);

        if (!this.isSilent() && this.getSettings().getSpinSound() != null) {
            this.getSettings().getSpinSound().play(this.getOpening().getPlayer());
        }
        this.onSpin();
        this.spinCount++;

        // Slowdown Spinner
        if (this.getSettings().getSpinSlowdownStep() > 0 && this.getCurrentSpins() > 0) {
            if (this.getCurrentSpins() % this.getSettings().getSpinSlowdownStep() == 0) {
                this.spinSpeedTicks += this.getSettings().getSpinSlowdownAmount();
            }
        }
    }

    @Override
    public int getTotalSpins() {
        return this.getSettings().getSpinTimes();
    }

    @Override
    public long getCurrentSpins() {
        return this.spinCount;
    }

    @Override
    public void setCurrentSpins(long spins) {
        this.spinCount = Math.max(0, spins);
    }

    @Override
    public long getInterval() {
        return this.getSpinSpeedTicks();
    }

    @Override
    public boolean isCompleted() {
        return this.getTotalSpins() >= 0 && this.getCurrentSpins() >= this.getTotalSpins();// || (this.getCurrentSpins() > 0 && !this.isRunning());
    }

    public long getSpinSpeedTicks() {
        return spinSpeedTicks;
    }
}
