package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.impl.DummyEffect;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public abstract class CrateEffect {

    protected final String id;
    protected final long tickInterval;
    protected final int  maxSteps;

    protected long tickCount;

    public CrateEffect(@NotNull String id, long tickInterval, int maxSteps) {
        this.id = id;
        this.tickCount = 0L;
        this.tickInterval = Math.max(1L, tickInterval);
        this.maxSteps = Math.max(0, maxSteps);
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public abstract String getName();

    public boolean isDummy() {
        return this == DummyEffect.INSTANCE;
    }

    public void complete() {
        this.tickCount = -10L; // Do a 0.5s pause when effect is finished.
    }

    public void addTickCount() {
        this.tickCount++;
    }

    public void playStep(@NotNull Location location, @NotNull UniParticle particle, @NotNull Player player) {
        // Do not play an effect while paused.
        if (this.tickCount < 0 || this.tickCount % this.tickInterval != 0) return;

        this.onStepPlay(LocationUtil.setCenter2D(location.clone()), particle, (int) this.tickCount, player);

        if (this.tickCount >= this.maxSteps) {
            this.complete();
        }
    }

    public abstract void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player);

    @NotNull
    public static Location getPointOnCircle(@NotNull Location location, boolean doCopy, double x, double z, double y) {
        return (doCopy ? location.clone() : location).add(Math.cos(x) * z, y, Math.sin(x) * z);
    }

    public final long getTickInterval() {
        return this.tickInterval;
    }

    public final int getMaxSteps() {
        return this.maxSteps;
    }
}
