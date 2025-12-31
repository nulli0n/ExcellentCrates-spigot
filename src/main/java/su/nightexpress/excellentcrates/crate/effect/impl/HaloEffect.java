package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class HaloEffect extends CrateEffect {

    public HaloEffect() {
        super(EffectId.HALO, 2L, 24);
    }

    @Override
    @NotNull
    public String getName() {
        return "Halo";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double radius = 0.8;
        double y = 1.8;
        double angle = step * 0.25;

        Location loc = origin.clone().add(
                Math.cos(angle) * radius,
                y,
                Math.sin(angle) * radius
        );

        particle.play(player, loc, 0, 1);
    }
}
