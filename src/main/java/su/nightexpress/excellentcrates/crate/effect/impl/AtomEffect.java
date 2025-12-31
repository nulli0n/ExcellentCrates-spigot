package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class AtomEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 1.3;  // How big the rings are
    private static final int POINTS = 20;      // Particles per ring (higher = smoother line)
    private static final int RINGS = 3;        // Number of rings (3 makes the atom shape)
    private static final double SPEED = 2.5;   // Rotation speed

    public AtomEffect() {
        // ID, Delay (ticks), Max Steps (animation frames)
        // We use 180 steps for a smooth full rotation loop
        super(EffectId.ATOM, 1L, 180);
    }

    @Override
    @NotNull
    public String getName() {
        // You can add this key to your Lang file or just return "Atom"
        return "Atom";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // Calculate the rotation of the entire system based on the current step
        double animationAngle = Math.toRadians(step * SPEED);

        // We create 3 rings
        for (int ringIndex = 0; ringIndex < RINGS; ringIndex++) {

            // Calculate the fixed offset for this specific ring (0, 60, 120 degrees)
            double ringOffset = Math.toRadians(ringIndex * (180.0 / RINGS));

            // Combine animation rotation + ring offset
            double totalAngle = animationAngle + ringOffset;

            // Draw the particles for this ring
            for (int i = 0; i < POINTS; i++) {
                // Calculate position on a standard vertical circle (2D)
                double theta = ((double) i / POINTS) * Math.PI * 2;
                double x = RADIUS * Math.cos(theta);
                double y = RADIUS * Math.sin(theta);
                double z = 0;

                // Create a vector for this point
                Vector vec = new Vector(x, y, z);

                // KEY STEP: Rotate this flat vertical circle around the Y axis
                // This turns the 2D circle into one of the 3D intersecting atomic rings
                rotateAroundY(vec, totalAngle);

                // Add to crate location (adjust Y + 0.5 to center it on the block)
                Location loc = origin.clone().add(vec.getX(), vec.getY() + 0.5, vec.getZ());

                particle.play(player, loc, 0f, 0f, 1);
            }
        }
    }

    /**
     * Helper method to rotate a vector around the Y axis (Yaw)
     * Standard Bukkit/Spigot math.
     */
    private void rotateAroundY(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        vector.setX(x).setZ(z);
    }
}