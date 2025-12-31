package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;
import java.util.concurrent.ThreadLocalRandom;

public class RainEffect extends CrateEffect {

    private static final double RADIUS = 1.2;
    private static final double TOP_HEIGHT = 3.5;

    public RainEffect() {
        // Run slightly slower delay (2 ticks) so it's not too overwhelming,
        // or keep 1L for heavy rain.
        super(EffectId.RAIN, 1L, 100);
    }

    @Override
    @NotNull
    public String getName() {
        return "Rain";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // Spawn multiple drops per tick to simulate density
        for (int i = 0; i < 6; i++) {
            double r = ThreadLocalRandom.current().nextDouble() * RADIUS;
            double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;

            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            // Random height between 0.5 (near crate) and 3.5 (air)
            // This creates a "filled" column of particles immediately
            double y = 0.5 + (ThreadLocalRandom.current().nextDouble() * TOP_HEIGHT);

            particle.play(player, origin.clone().add(x, y, z), 0, 0, 0);
        }

        // Optional: Create a "splash" ring on the ground occasionally
        if (step % 5 == 0) {
            double r = ThreadLocalRandom.current().nextDouble() * RADIUS;
            double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;
            // Uses a small Y offset (0.2) to be just above floor
            particle.play(player, origin.clone().add(x, 0.2, z), 0, 0, 0);
        }
    }
}