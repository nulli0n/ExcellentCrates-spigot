package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.AbstractEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CrateSimpleEffect extends AbstractEffect {

    public CrateSimpleEffect() {
        super(2L, 2);
    }

    @Override
    public void doStep(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location location = origin.add(0, 0.5D, 0);
        particle.play(player, location, 0.3f, 0.1f, 30);
    }
}
