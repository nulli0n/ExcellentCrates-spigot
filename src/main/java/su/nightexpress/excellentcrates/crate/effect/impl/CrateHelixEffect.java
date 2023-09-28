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
    public void doStep(@NotNull Location loc2, @NotNull UniParticle particle, int step) {
        Location location = loc2.add(0, 0.05D, 0);

        double n2 = 0.3141592653589793 * step;
        double n3 = step * 0.1 % 2.5;
        double n4 = 0.75;
        Location pointOnCircle = getPointOnCircle(location, true, n2, n4, n3);
        Location pointOnCircle2 = getPointOnCircle(location, true, n2 - 3.141592653589793, n4, n3);
        particle.play(pointOnCircle, 0.0, 1);
        particle.play(pointOnCircle2, 0.0, 1);
    }
}
