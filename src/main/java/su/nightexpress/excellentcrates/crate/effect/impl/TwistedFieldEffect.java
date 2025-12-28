package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class TwistedFieldEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 1.3;
    private static final double HEIGHT = 1.5;
    private static final int STRANDS = 8;
    private static final double SLANT = 1.0;
    private static final double WAVE_CURVE = 0.0;
    private static final double ROTATION_SPEED = -1.2;
    private static final double DENSITY = 0.25;

    public TwistedFieldEffect() {
        super(EffectId.TWISTED_FIELD, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Twisted Field";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double globalRot = Math.toRadians(step * ROTATION_SPEED);

        for (int i = 0; i < STRANDS; i++) {
            double strandOffset = Math.toRadians(i * (360.0 / STRANDS));
            for (double y = 0; y <= HEIGHT; y += DENSITY) {
                double twistOffset = y * SLANT;
                double waveOffset = Math.sin(y * 3.0) * WAVE_CURVE;
                double finalAngle = globalRot + strandOffset + twistOffset + waveOffset;
                double x = Math.cos(finalAngle) * RADIUS;
                double z = Math.sin(finalAngle) * RADIUS;

                Location loc = origin.clone().add(x, 0.2 + y, z);

                particle.play(player, loc, 0, 0, 0);
            }
        }
    }
}