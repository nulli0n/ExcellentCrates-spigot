package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class ForceFieldEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 1.3;
    private static final double HEIGHT = 2.5;
    private static final int STRANDS = 10;
    private static final double ROTATION_SPEED = 2.0;
    private static final double VERTICAL_SPEED = 0.1;
    private static final double DENSITY = 0.2;

    public ForceFieldEffect() {
        super(EffectId.FORCE_FIELD, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Force Field";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double rotation = Math.toRadians(step * ROTATION_SPEED);
        double yOffset = (step * VERTICAL_SPEED) % DENSITY;
        for (int i = 0; i < STRANDS; i++) {
            double angle = rotation + Math.toRadians(i * (360.0 / STRANDS));

            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;

            for (double y = yOffset; y <= HEIGHT; y += DENSITY) {
                Location loc = origin.clone().add(x, 0.2 + y, z);
                particle.play(player, loc, 0, 0, 0);
            }
        }
    }
}