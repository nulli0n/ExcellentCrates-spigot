package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CratePulsarEffect extends AbstractEffect {

    public CratePulsarEffect() {
        super(2L, 38);
    }

    @Override
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step) {
        Location loc = origin.clone().add(0, -0.8D, 0);
        double y = (0.5 + step * 0.15) % 3.0;
        for (int point = 0; point < y * 10.0; ++point) {
            double x = 6.283185307179586 / (y * 10.0) * point;
            Location location = getPointOnCircle(loc.clone(), false, x, y, 1.0);
            playSafe(location, player -> particle.play(player, location, 0.1f, 0.0f, 2));
        }
    }
}
