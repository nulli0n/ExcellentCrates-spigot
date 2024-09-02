package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public abstract class AbstractEffect {

    protected final long interval;
    protected final int  duration;
    protected int  step;
    protected int  count;

    public AbstractEffect(long interval, int duration) {
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

    public void step(@NotNull Location location, @NotNull UniParticle particle, @NotNull Player player) {
        /*if (this.step < 0) {
            this.step++;
        }*/

        // Do not play an effect while paused.
        if (this.count++ % (int) this.getInterval() != 0) return;
        if (this.step < 0) return;

        this.doStep(LocationUtil.setCenter2D(location.clone()), particle, this.step, player);

        // Do a 0.5s pause when particle effect is finished.
        if (this.step/*++*/ >= this.getDuration()) {
            this.step = -10;
            this.count = 0;
        }
    }

    public abstract void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player);

    @NotNull
    public static Location getPointOnCircle(@NotNull Location location, boolean doCopy, double x, double z, double y) {
        return (doCopy ? location.clone() : location).add(Math.cos(x) * z, y, Math.sin(x) * z);
    }

    public final long getInterval() {
        return this.interval;
    }

    public final int getDuration() {
        return this.duration;
    }
}
