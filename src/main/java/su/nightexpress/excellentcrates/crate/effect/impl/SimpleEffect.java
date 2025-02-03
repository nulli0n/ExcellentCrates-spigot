package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class SimpleEffect extends CrateEffect {

    public SimpleEffect() {
        super(2L, 2);
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        Location location = origin.add(0, 0.5D, 0);
        particle.play(player, location, 0.3f, 0.1f, 30);
    }
}
