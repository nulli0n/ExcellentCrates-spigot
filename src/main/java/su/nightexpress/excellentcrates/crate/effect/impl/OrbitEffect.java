package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class OrbitEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 2.5;    // Radius: 2.5 blocks away from center (5 block wide circle)
    private static final int SATELLITES = 3;     // Number of orbiting points
    private static final double SPEED = 3.0;     // How fast they orbit
    private static final double VERTICAL_AMP = 1.0; // How high/low they bob (Sine wave height)
    private static final double VERTICAL_SPEED = 4.0; // How fast they bob up and down

    public OrbitEffect() {
        // ID, Delay (1 tick), Max Steps (360 degrees)
        super(EffectId.ORBIT, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Orbit";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // We calculate positions for 3 satellites evenly spaced
        for (int i = 0; i < SATELLITES; i++) {

            // 1. Calculate the horizontal Angle
            // 'step * SPEED' rotates it over time
            // 'i * (360 / SATELLITES)' spaces them apart (0, 120, 240 degrees)
            double angle = Math.toRadians((step * SPEED) + (i * (360.0 / SATELLITES)));

            // 2. Calculate X and Z (Horizontal Circle)
            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;

            // 3. Calculate Y (Vertical Sine Wave)
            // This makes them float up and down smoothly so it's not just a flat ring
            double yOffset = Math.sin(Math.toRadians(step * VERTICAL_SPEED)) * VERTICAL_AMP;

            // Add 1.0 to Y so it centers on the block, not the floor
            Location location = origin.clone().add(x, 1.0 + yOffset, z);

            // Play the particle
            particle.play(player, location, 0, 0, 0);
        }
    }
}