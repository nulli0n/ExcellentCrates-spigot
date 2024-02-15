package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;

public class CrateSimpleEffect extends CrateEffect {

    public CrateSimpleEffect() {
        super(2L, 2);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull UniParticle particle, int step) {
        particle.play(location.add(0, 0.5D, 0), 0.3f, 0.1f, 30);
    }
}
