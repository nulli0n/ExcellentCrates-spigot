package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CelestialBandsEffect extends CrateEffect {

    public CelestialBandsEffect() {
        super(EffectId.CELESTIAL_BANDS, 1L, 40);
    }

    @Override
    @NotNull
    public String getName() {
        return "Celestial Bands";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double t = step * 0.2;

        for (int i = 0; i < 2; i++) {
            double radius = 0.9;
            double y = 0.7 + i * 0.5;

            Location loc = origin.clone().add(
                    Math.cos(t + i * Math.PI) * radius,
                    y,
                    Math.sin(t + i * Math.PI) * radius
            );

            particle.play(player, loc, 0, 1);
        }
    }
}