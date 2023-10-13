package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.values.UniParticle;

public abstract class CrateEffect {

    protected final long interval;
    protected final int  duration;
    protected int  step;
    protected int  count;

    public CrateEffect(long interval, int duration) {
        this.step = 0;
        this.count = 0;
        this.interval = interval;
        this.duration = duration;
    }

    public void reset() {
        this.step = 0;
        this.count = 0;
    }

    public void addStep() {
        if (++this.step > this.getDuration()) {
            this.reset();
        }
    }

    public void step(@NotNull Location location, @NotNull UniParticle particle) {
        /*if (this.step < 0) {
            this.step++;
        }*/

        // Do not play an effect while paused.
        if (this.count++ % (int) this.getInterval() != 0) return;
        if (this.step < 0) return;

        this.doStep(LocationUtil.getCenter(location.clone(), false), particle, this.step);

        // Do a 0.5s pause when particle effect is finished.
        if (this.step/*++*/ >= this.getDuration()) {
            this.step = -10;
            this.count = 0;
        }
    }

    public abstract void doStep(@NotNull Location location, @NotNull UniParticle particle, int step);

    @NotNull
    public static Location getPointOnCircle(@NotNull Location loc, boolean doCopy, double x, double z, double y) {
        return (doCopy ? loc.clone() : loc).add(Math.cos(x) * z, y, Math.sin(x) * z);
    }

    public final long getInterval() {
        return this.interval;
    }

    public final int getDuration() {
        return this.duration;
    }
}
