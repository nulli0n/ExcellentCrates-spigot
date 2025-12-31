package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class RingEffect extends CrateEffect {

    public RingEffect() {
        super(EffectId.RING, 2L, 20);
    }

    @Override
    @NotNull
    public String getName() {
        return "Ring";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double radius = 1.2;
        double y = 0.2;

        for (int i = 0; i < 12; i++) {
            double angle = (2 * Math.PI / 12) * i + step * 0.1;

            Location loc = origin.clone().add(
                    Math.cos(angle) * radius,
                    y,
                    Math.sin(angle) * radius
            );

            particle.play(player, loc, 0, 1);
        }
    }
}
