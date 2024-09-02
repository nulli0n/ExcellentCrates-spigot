package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CrateHelixEffect extends AbstractEffect {

    public CrateHelixEffect() {
        super(1L, 24);
    }

    @Override
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location location = origin.add(0, 0.05D, 0);

        double x = 0.3141592653589793 * step;
        double z = step * 0.1 % 2.5;
        double y = 0.75;

        Location left = getPointOnCircle(location, true, x, y, z);
        Location right = getPointOnCircle(location, true, x - Math.PI, y, z);

        particle.play(player, left, 0, 1);
        particle.play(player, right, 0, 1);
    }
}
