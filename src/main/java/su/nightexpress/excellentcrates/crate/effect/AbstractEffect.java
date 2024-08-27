package su.nightexpress.excellentcrates.crate.effect;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractEffect {

    protected final long interval;
    protected final int duration;
    protected int step;
    protected int count;

    public AbstractEffect(long interval, int duration) {
        this.step = 0;
        this.count = 0;
        this.interval = interval;
        this.duration = duration;
    }

    @NotNull
    public static Location getPointOnCircle(@NotNull Location location, boolean doCopy, double x, double z, double y) {
        return (doCopy ? location.clone() : location).add(Math.cos(x) * z, y, Math.sin(x) * z);
    }

    protected static void playSafe(@NotNull Location location, @NotNull Consumer<Player> consumer) {
        World world = location.getWorld();
        if (world == null) return;

        Set<Player> players = new HashSet<>(world.getPlayers());
        players.forEach(player -> {
            if (player == null || !player.isOnline()) return;

            consumer.accept(player);
        });
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

        this.doStep(LocationUtil.setCenter2D(location.clone()), particle, this.step);

        // Do a 0.5s pause when particle effect is finished.
        if (this.step/*++*/ >= this.getDuration()) {
            this.step = -10;
            this.count = 0;
        }
    }

    public abstract void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step);

    public final long getInterval() {
        return this.interval;
    }

    public final int getDuration() {
        return this.duration;
    }
}
