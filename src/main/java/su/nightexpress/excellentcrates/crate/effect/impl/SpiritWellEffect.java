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
        // We draw a circle at the BASE of the crate
        // The particles will naturally rise (if using smoke/flame) or just sit there
        int particles = 4;

        for (int i = 0; i < particles; i++) {
            // Random angle to make it look like natural gas/fire
            double angle = Math.random() * 2 * Math.PI;

            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;

            // Spawn at floor level (0.2)
            Location loc = origin.clone().add(x, 0.2, z);

            // We give it a tiny upward velocity (0.05) to encourage the "Well" rising effect
            // If you use REDSTONE/DUST, they will just sit and blink (sparkling floor)
            // If you use SMOKE/FLAME, they will rise up the crate sides
            particle.play(player, loc, 0, 0.05, 0);
        }

        // Occasional "Core" bubble in the center
        if (step % 10 == 0) {
            particle.play(player, origin.clone().add(0, 0.5, 0), 0, 0.1, 0);
        }
    }
}