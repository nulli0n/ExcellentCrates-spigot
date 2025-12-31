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
        double rotationOffset = Math.toRadians(step * SPEED);
        for (int i = 0; i < POINTS; i++) {
            double angle = rotationOffset + Math.toRadians(i * (360.0 / POINTS));
            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;
            particle.play(player, origin.clone().add(x, 0.2, z), 0, 0, 0);
            if (step % 5 == 0) {
                // Rising particle
                particle.play(player, origin.clone().add(x, 0.5 + (Math.sin(step * 0.1) * 0.5), z), 0, 0.1, 0);
            }
            double nextAngle = rotationOffset + Math.toRadians(((i + 2) % POINTS) * (360.0 / POINTS));
            double nextX = Math.cos(nextAngle) * RADIUS;
            double nextZ = Math.sin(nextAngle) * RADIUS;
            for (double d = 0; d < 1.0; d += 0.2) {
                double lineX = x + (nextX - x) * d;
                double lineZ = z + (nextZ - z) * d;
                particle.play(player, origin.clone().add(lineX, 0.2, lineZ), 0, 0, 0);
            }
        }
    }
}