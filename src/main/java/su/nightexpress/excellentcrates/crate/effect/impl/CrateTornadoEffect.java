package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.ArrayList;
import java.util.List;

public class CrateTornadoEffect extends AbstractEffect {

    private static final double Y_OFFSET           = 0.15D;
    private static final float  TORNADO_HEIGHT     = 3.15F;
    private static final float  MAX_TORNADO_RADIUS = 2.25F;
    private static final double DISTANCE           = 0.375D;

    public CrateTornadoEffect() {
        super(2L, 8);
    }

    @Override
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location loc = origin.clone().add(0.0D, 0.5D, 0.0D);
        double offset = 0.25D * (MAX_TORNADO_RADIUS * (2.35D / TORNADO_HEIGHT));
        double vertical = TORNADO_HEIGHT - DISTANCE * step;

        double radius = offset * vertical;
        if (radius > MAX_TORNADO_RADIUS) {
            radius = MAX_TORNADO_RADIUS;
        }
        for (Vector vector : this.createCircle(vertical, radius)) {
            Location location = loc.add(vector);
            particle.play(player, location, 0.1f, 0.0f, 3);
            loc.subtract(vector);
        }
        loc.subtract(0.0D, Y_OFFSET, 0.0D);
    }

    private List<Vector> createCircle(double vertical, double radius) {
        double amount = radius * 64.0D;
        double d2 = 2 * Math.PI / amount;
        List<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double d3 = i * d2;
            double cos = radius * Math.cos(d3);
            double sin = radius * Math.sin(d3);
            Vector vector = new Vector(cos, vertical, sin);
            vectors.add(vector);
        }
        return vectors;
    }
}
