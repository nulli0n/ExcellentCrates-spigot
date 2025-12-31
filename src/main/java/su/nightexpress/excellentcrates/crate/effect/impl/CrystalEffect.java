package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CrystalEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 0.9;
    private static final double HEIGHT = 1.3;
    private static final int PARTICLES = 5;

    public CrystalEffect() {
        super(EffectId.CRYSTAL, 2L, 180);
    }

    @Override
    @NotNull
    public String getName() {
        return "Crystal";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location center = origin.clone().add(0, 1.0, 0);

        double angle = Math.toRadians(step * 2);

        Vector top = new Vector(0, HEIGHT, 0);
        Vector bottom = new Vector(0, -HEIGHT, 0);

        Vector[] middlePoints = new Vector[4];
        for (int i = 0; i < 4; i++) {
            double theta = (Math.PI / 2) * i;
            middlePoints[i] = new Vector(Math.cos(theta) * RADIUS, 0, Math.sin(theta) * RADIUS);
        }
        rotateY(top, angle);
        rotateY(bottom, angle);
        for (Vector v : middlePoints) rotateY(v, angle);
        for (int i = 0; i < 4; i++) {
            Vector current = middlePoints[i];
            Vector next = middlePoints[(i + 1) % 4];
            drawLine(center, current, next, particle, player);
            drawLine(center, top, current, particle, player);
            drawLine(center, bottom, current, particle, player);
        }
    }

    private void drawLine(Location center, Vector v1, Vector v2, UniParticle particle, Player player) {
        double distance = v1.distance(v2);
        Vector dir = v2.clone().subtract(v1).normalize();
        double spacing = distance / PARTICLES;

        for (double d = 0; d <= distance; d += spacing) {
            Vector offset = dir.clone().multiply(d);
            Location loc = center.clone().add(v1).add(offset);
            particle.play(player, loc, 0, 0, 0);
        }
    }

    private void rotateY(Vector v, double angle) {
        double x = v.getX() * Math.cos(angle) - v.getZ() * Math.sin(angle);
        double z = v.getX() * Math.sin(angle) + v.getZ() * Math.cos(angle);
        v.setX(x).setZ(z);
    }
}