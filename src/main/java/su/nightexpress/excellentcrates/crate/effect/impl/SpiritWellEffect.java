package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class SpiritWellEffect extends CrateEffect {

    private static final double RADIUS = 1.0;

    public SpiritWellEffect() {
        super(EffectId.SPIRIT_WELL, 2L, 100); // Slower tick rate (2L) is fine because they linger
    }

    @Override
    @NotNull
    public String getName() {
        return "SpiritWell";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        int particles = 4;
        for (int i = 0; i < particles; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;
            Location loc = origin.clone().add(x, 0.2, z);
            particle.play(player, loc, 0, 0.05, 0);
        }
        if (step % 10 == 0) {
            particle.play(player, origin.clone().add(0, 0.5, 0), 0, 0.1, 0);
        }
    }
}