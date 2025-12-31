package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class GalaxyEffect extends CrateEffect {

    private static final int ARMS = 3;
    private static final double MAX_RADIUS = 2.5;
    private static final double ROTATION_SPEED = 4.0;
    private static final double TIGHTNESS = 0.5; // How much the arms curl

    public GalaxyEffect() {
        super(EffectId.GALAXY, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Galaxy";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double rotationOffset = Math.toRadians(step * ROTATION_SPEED);

        // Generate the galaxy arms
        for (int arm = 0; arm < ARMS; arm++) {
            double armOffset = (Math.PI * 2 * arm) / ARMS;

            // Draw a line of particles moving outwards to form the arm
            // 20 points per arm
            for (int i = 1; i < 20; i++) {
                double distance = (double) i / 20 * MAX_RADIUS;

                // The angle increases as distance increases (creating the spiral curve)
                // 'distance * TIGHTNESS' twists the arm
                double angle = armOffset + rotationOffset + (distance * TIGHTNESS);

                double x = Math.cos(angle) * distance;
                double z = Math.sin(angle) * distance;

                // Slight incline toward the center (black hole shape)
                double y = 1.0 - (distance * 0.2);

                particle.play(player, origin.clone().add(x, y, z), 0, 0, 0);
            }
        }

        // Center "Core" pulsing
        if (step % 5 == 0) {
            particle.play(player, origin.clone().add(0, 1.0, 0), 0, 0.1, 0);
        }
    }
}