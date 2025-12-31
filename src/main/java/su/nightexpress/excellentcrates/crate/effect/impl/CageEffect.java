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
    private static final double SIZE = 1.5;    // Distance from center to corners (Total width = 3 blocks)
    private static final int PARTICLES = 8;    // Particles per edge line (Higher = solid line)
    private static final double ROTATION_SPEED = 1.5; // How fast the cube spins

    public CageEffect() {
        // ID, Delay (2 ticks), Max Steps (360 degrees)
        super(EffectId.CAGE, 2L, 240);
    }

    @Override
    @NotNull
    public String getName() {
        return "Cage";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // Center the cage slightly up so the crate is in the middle of the cube
        Location center = origin.clone().add(0, 1.0, 0);

        // Calculate rotation for this frame
        double angle = Math.toRadians(step * ROTATION_SPEED);

        // Define the 8 corners of a cube relative to (0,0,0)
        // Format: Vector(X, Y, Z)
        Vector[] corners = new Vector[] {
                new Vector(SIZE, SIZE, SIZE),   // 0: Top-Right-Front
                new Vector(SIZE, SIZE, -SIZE),  // 1: Top-Right-Back
                new Vector(-SIZE, SIZE, -SIZE), // 2: Top-Left-Back
                new Vector(-SIZE, SIZE, SIZE),  // 3: Top-Left-Front
                new Vector(SIZE, -SIZE, SIZE),  // 4: Bottom-Right-Front
                new Vector(SIZE, -SIZE, -SIZE), // 5: Bottom-Right-Back
                new Vector(-SIZE, -SIZE, -SIZE),// 6: Bottom-Left-Back
                new Vector(-SIZE, -SIZE, SIZE)  // 7: Bottom-Left-Front
        };

        // Rotate every corner to match the current spin
        for (Vector v : corners) {
            rotateY(v, angle);
        }

        // Define the 12 edges (connections between corners)
        // We assume corners are indexed 0-7 as above.
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Top Face
                {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Bottom Face
                {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Pillars connecting Top to Bottom
        };

        // Draw lines for each edge
        for (int[] pair : edges) {
            Vector start = corners[pair[0]];
            Vector end = corners[pair[1]];
            drawLine(center, start, end, particle, player);
        }
    }

    /**
     * Draws a straight line of particles between two points.
     */
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

    /**
     * Rotates a vector around the Y axis.
     */
    private void rotateY(Vector v, double angle) {
        double x = v.getX() * Math.cos(angle) - v.getZ() * Math.sin(angle);
        double z = v.getX() * Math.sin(angle) + v.getZ() * Math.cos(angle);
        v.setX(x).setZ(z);
    }
}