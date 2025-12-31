package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class HeartOfTheForestEffect extends CrateEffect {

    public HeartOfTheForestEffect() {
        super(EffectId.HEART_OF_FOREST, 1L, 120);
    }

    @Override
    public @NotNull String getName() {
        return "Heart of the Forest";
    }

    @Override
    public void onStepPlay(@NotNull Location o, @NotNull UniParticle p, int step, @NotNull Player player) {

        // Phase 1: calm life energy
        if (step < 60) {
            double t = step * 0.12;

            Location loc = o.clone().add(
                    Math.cos(t) * 0.9,
                    0.8,
                    Math.sin(t) * 0.9
            );

            p.play(player, loc, 0, 1);
        }

        // Phase 2: awakening
        else if (step < 100) {
            double t = step * 0.25;

            for (int i = 0; i < 8; i++) {
                double a = Math.PI * 2 * i / 8;

                Location loc = o.clone().add(
                        Math.cos(a + t) * 1.4,
                        1.0 + Math.sin(t) * 0.5,
                        Math.sin(a + t) * 1.4
                );

                p.play(player, loc, 0, 1);
            }
        }

        // Phase 3: divine heart
        else {
            double pulse = Math.sin(step * 0.3) * 0.4;
            p.play(player, o.clone().add(0, 1.6 + pulse, 0), 0, 4);
        }
    }
}
