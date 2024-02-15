package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;

public class CrateHelixEffect extends CrateEffect {

    public CrateHelixEffect() {
        super(1L, 24);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull UniParticle particle, int step) {
        Location location2 = location.add(0, 0.05D, 0);

        double x = 0.3141592653589793 * step;
        double z = step * 0.1 % 2.5;
        double y = 0.75;
        Location pointOnCircle = getPointOnCircle(location2, true, x, y, z);
        Location pointOnCircle2 = getPointOnCircle(location2, true, x - Math.PI, y, z);
        particle.play(pointOnCircle, 0, 1);
        particle.play(pointOnCircle2, 0, 1);
    }
}
