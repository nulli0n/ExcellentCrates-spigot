package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class OrbitEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 2.5;
    private static final int SATELLITES = 3;
    private static final double SPEED = 3.0;
    private static final double VERTICAL_AMP = 1.0;
    private static final double VERTICAL_SPEED = 4.0;

    public OrbitEffect() {
        super(EffectId.ORBIT, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Orbit";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        for (int i = 0; i < SATELLITES; i++) {
            double angle = Math.toRadians((step * SPEED) + (i * (360.0 / SATELLITES)));
            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;
            double yOffset = Math.sin(Math.toRadians(step * VERTICAL_SPEED)) * VERTICAL_AMP;
            Location location = origin.clone().add(x, 1.0 + yOffset, z);
            particle.play(player, location, 0, 0, 0);
        }
    }
}