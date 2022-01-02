package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.effect.ICrateBlockEffect;

public abstract class AbstractCrateBlockEffect extends BukkitRunnable implements ICrateBlockEffect {

    protected static final ExcellentCrates PLUGIN = ExcellentCrates.getPlugin(ExcellentCrates.class);

    protected CrateEffectModel model;
    protected int              step;
    protected long             interval;
    protected int              duration;
    private   int              count;

    public AbstractCrateBlockEffect(@NotNull CrateEffectModel model, long interval, int duration) {
        this.model = model;
        this.step = 0;
        this.count = 0;
        this.interval = interval;
        this.duration = duration;
    }

    @Override
    public void run() {
        if (this.step < 0) {
            this.step++;
        }

        // Do not play an effect while paused.
        if (this.count++ % (int) this.getInterval() != 0) return;
        if (this.step < 0) return;

        PLUGIN.getCrateManager().getCrates().stream().filter(crate -> crate.getBlockEffect().getModel() == this.model).forEach(crate -> {
            CrateEffectSettings effect = crate.getBlockEffect();
            crate.getBlockLocations().forEach(loc -> {
                this.doStep(LocationUtil.getCenter(loc, false), effect.getParticleName(), effect.getParticleData(), this.step);
            });
        });

        // Do a 0.5s pause when particle effect is finished.
        if (this.step++ >= this.getDuration()) {
            this.step = -(20 / 2);
            this.count = 0;
        }
    }

    @Override
    public final void start() {
        this.runTaskTimerAsynchronously(PLUGIN, 0L, 1L);
    }

    @Override
    public final long getInterval() {
        return this.interval;
    }

    @Override
    public final int getDuration() {
        return this.duration;
    }
}
