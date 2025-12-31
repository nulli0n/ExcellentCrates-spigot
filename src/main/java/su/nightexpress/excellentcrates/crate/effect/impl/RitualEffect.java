package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class RitualEffect extends CrateEffect {

    private static final double RADIUS = 2.0;
    private static final int POINTS = 5;       // Pentagram
    private static final double SPEED = 2.0;   // Rotation speed

    public RitualEffect() {
        super(EffectId.RITUAL, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Ritual";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // We handle the rotation of the entire star
        double rotationOffset = Math.toRadians(step * SPEED);

        // 1. Draw the Star Points + Pillars
        for (int i = 0; i < POINTS; i++) {
            // Distribute points evenly (72 degrees for a pentagram)
            double angle = rotationOffset + Math.toRadians(i * (360.0 / POINTS));

            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;

            // Draw the point on the floor
            particle.play(player, origin.clone().add(x, 0.2, z), 0, 0, 0);

            // Draw a "pillar" rising from this point (every 5th step to save performance)
            if (step % 5 == 0) {
                // Rising particle
                particle.play(player, origin.clone().add(x, 0.5 + (Math.sin(step * 0.1) * 0.5), z), 0, 0.1, 0);
            }

            // 2. Draw lines connecting to the "next" star point to form the star shape
            // In a pentagram, you connect point i to i+2
            double nextAngle = rotationOffset + Math.toRadians(((i + 2) % POINTS) * (360.0 / POINTS));
            double nextX = Math.cos(nextAngle) * RADIUS;
            double nextZ = Math.sin(nextAngle) * RADIUS;

            // Interpolate a few particles between current point and target point
            for (double d = 0; d < 1.0; d += 0.2) {
                double lineX = x + (nextX - x) * d;
                double lineZ = z + (nextZ - z) * d;
                particle.play(player, origin.clone().add(lineX, 0.2, lineZ), 0, 0, 0);
            }
        }
    }
}