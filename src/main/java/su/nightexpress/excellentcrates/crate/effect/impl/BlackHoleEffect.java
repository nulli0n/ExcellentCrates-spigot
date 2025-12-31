package su.nightexpress.excellentcrates.crate.effect.impl;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class BlackHoleEffect extends CrateEffect {

    public BlackHoleEffect() {
        super(EffectId.BLACK_HOLE, 1L, 40);
    }

    @Override
    @NotNull
    public String getName() {
        return "Black Hole";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double progress = 1D - (step / 40D);
        double radius = 2.5 * progress;
        double angle = step * 0.5;

        for (int i = 0; i < 12; i++) {
            double a = angle + (Math.PI * 2 / 12) * i;

            Location loc = origin.clone().add(
                    Math.cos(a) * radius,
                    0.4,
                    Math.sin(a) * radius
            );

            particle.play(player, loc, 0, 1);
        }
    }
}

