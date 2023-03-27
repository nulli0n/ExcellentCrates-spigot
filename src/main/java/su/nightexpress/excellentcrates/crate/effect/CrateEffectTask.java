package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;

import java.util.stream.Collectors;

public abstract class CrateEffectTask extends BukkitRunnable {

    protected static final ExcellentCrates PLUGIN = ExcellentCratesAPI.PLUGIN;

    protected CrateEffectModel model;
    protected int              step;
    protected long             interval;
    protected int              duration;
    private   int              count;

    public CrateEffectTask(@NotNull CrateEffectModel model, long interval, int duration) {
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

        PLUGIN.getCrateManager().getCrates().stream().filter(crate -> crate.getBlockEffectModel() == this.model).forEach(crate -> {
            crate.getBlockLocations().stream().collect(Collectors.toUnmodifiableSet()).forEach(location -> {
                World world = location.getWorld();
                int chunkX = location.getBlockX() >> 4;
                int chunkZ = location.getBlockZ() >> 4;
                if (world == null || !world.isChunkLoaded(chunkX, chunkZ)) return;

                this.doStep(LocationUtil.getCenter(location.clone(), false), crate.getBlockEffectParticle(), this.step);
            });
        });

        // Do a 0.5s pause when particle effect is finished.
        if (this.step++ >= this.getDuration()) {
            this.step = -(20 / 2);
            this.count = 0;
        }
    }

    public final void start() {
        this.runTaskTimerAsynchronously(PLUGIN, 0L, 1L);
    }

    public abstract void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step);

    public final long getInterval() {
        return this.interval;
    }

    public final int getDuration() {
        return this.duration;
    }
}
