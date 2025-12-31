package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class FountainEffect extends CrateEffect {

    public FountainEffect() {
        super(EffectId.FOUNTAIN, 1L, 18);
    }

    @Override
    @NotNull
    public String getName() {
        return "Fountain";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double radius = 0.4 + (step * 0.02);
        double y = 0.6 + Math.sin(step * 0.3) * 0.4;

        for (int i = 0; i < 6; i++) {
            double angle = (2 * Math.PI / 6) * i;

            Location loc = origin.clone().add(
                    Math.cos(angle) * radius,
                    y,
                    Math.sin(angle) * radius
            );

            particle.play(player, loc, 0, 1);
        }
    }
}
