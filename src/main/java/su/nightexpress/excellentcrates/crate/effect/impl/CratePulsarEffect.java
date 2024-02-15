package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;

public class CratePulsarEffect extends CrateEffect {

    public CratePulsarEffect() {
        super(2L, 38);
    }

    @Override
    public void doStep(@NotNull Location loc2, @NotNull UniParticle particle, int step) {
        Location loc = loc2.clone().add(0, -0.8D, 0);
        double n2 = (0.5 + step * 0.15) % 3.0;
        for (int n3 = 0; n3 < n2 * 10.0; ++n3) {
            double n4 = 6.283185307179586 / (n2 * 10.0) * n3;
            particle.play(getPointOnCircle(loc.clone(), false, n4, n2, 1.0), 0.1f, 0.0f, 2);
        }
    }
}
