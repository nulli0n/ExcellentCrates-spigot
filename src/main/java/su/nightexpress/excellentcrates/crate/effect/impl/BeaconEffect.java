package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class BeaconEffect extends CrateEffect {

    public BeaconEffect() {
        super(EffectId.BEACON, 3L, 40);
    }

    @Override
    @NotNull
    public String getName() {
        return Lang.EFFECT_MODEL_BEACON.text();
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double x = 2 * Math.PI / 7D * step;
        for (int yStep = step; yStep > Math.max(0, step - 25); --yStep) {
            Location location = getPointOnCircle(origin, true, x, 0.55, yStep * 0.75);

            particle.play(player, location, 0.0f, 0.15f, 0.0f, 0.0f, 4);
        }
    }
}
