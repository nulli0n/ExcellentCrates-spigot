package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CratePulsarEffect extends AbstractEffect {

    public CratePulsarEffect() {
        super(2L, 38);
    }

    @Override
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location shifted = origin.clone().add(0, -0.8D, 0);
        double y = (0.5 + step * 0.15) % 3.0;
        for (int point = 0; point < y * 10.0; ++point) {
            double x = 2 * Math.PI / (y * 10.0) * point;
            Location location = getPointOnCircle(shifted.clone(), false, x, y, 1.0);
            particle.play(player, location, 0.1f, 0.0f, 2);
        }
    }
}
