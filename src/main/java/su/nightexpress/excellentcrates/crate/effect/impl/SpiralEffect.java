package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class SpiralEffect extends CrateEffect {

    private static final double RADIUS = 1.0;
    private static final double VERTICAL_SPACING = 0.1;
    private static final double START_ANGLE = 0.0;
    private static final double END_ANGLE = 6 * Math.PI;
    private static final int NUM_POINTS = 50;

    public SpiralEffect() {
        super(EffectId.SPIRAL, 1L, NUM_POINTS);
    }

    @Override
    @NotNull
    public String getName() {
        return Lang.EFFECT_MODEL_SPIRAL.text();
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double deltaAngle = (END_ANGLE - START_ANGLE) / NUM_POINTS;
        double angle = START_ANGLE + step * deltaAngle;
        double x = RADIUS * Math.cos(angle);
        double z = RADIUS * Math.sin(angle);
        double y = VERTICAL_SPACING * angle;
        Location location = origin.clone().add(x, y, z);

        particle.play(player, location, 0f, 0f, 5);
    }
}
