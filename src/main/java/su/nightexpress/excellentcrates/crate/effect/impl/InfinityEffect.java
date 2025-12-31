package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class InfinityEffect extends CrateEffect {

    private static final double SCALE = 2.0; // Size of the figure-8
    private static final double SPEED = 3.0;

    public InfinityEffect() {
        super(EffectId.INFINITY, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Infinity";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // We spawn a trail of particles (e.g., 3 particles per tick) to make it look smooth
        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians((step * SPEED) + (i * 2));

            // Lemniscate of Bernoulli formula
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double denominator = 1 + (sin * sin);

            double x = SCALE * cos / denominator;
            double z = SCALE * cos * sin / denominator;

            // Add a slight bobbing motion so it's not perfectly flat
            double y = 1.5 + (Math.sin(angle * 2) * 0.3);

            particle.play(player, origin.clone().add(x, y, z), 0, 0, 0);
        }
    }
}