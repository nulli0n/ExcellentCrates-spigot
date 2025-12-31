package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CageEffect extends CrateEffect {

    // SETTINGS
    private static final double SIZE = 1.5;
    private static final int PARTICLES = 8;
    private static final double ROTATION_SPEED = 1.5;

    public CageEffect() {
        super(EffectId.CAGE, 2L, 240);
    }

    @Override
    @NotNull
    public String getName() {
        return "Cage";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location center = origin.clone().add(0, 1.0, 0);
        double angle = Math.toRadians(step * ROTATION_SPEED);
        Vector[] corners = new Vector[] {
                new Vector(SIZE, SIZE, SIZE),
                new Vector(SIZE, SIZE, -SIZE),
                new Vector(-SIZE, SIZE, -SIZE),
                new Vector(-SIZE, SIZE, SIZE),
                new Vector(SIZE, -SIZE, SIZE),
                new Vector(SIZE, -SIZE, -SIZE),
                new Vector(-SIZE, -SIZE, -SIZE),
                new Vector(-SIZE, -SIZE, SIZE)
        };

        for (Vector v : corners) {
            rotateY(v, angle);
        }

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Top Face
                {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Bottom Face
                {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Pillars connecting Top to Bottom
        };

        for (int[] pair : edges) {
            Vector start = corners[pair[0]];
            Vector end = corners[pair[1]];
            drawLine(center, start, end, particle, player);
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