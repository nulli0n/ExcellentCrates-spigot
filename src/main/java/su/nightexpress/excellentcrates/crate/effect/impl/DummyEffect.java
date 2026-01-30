package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class DummyEffect extends CrateEffect {

    public static final DummyEffect INSTANCE = new DummyEffect();

    public DummyEffect() {
        super(EffectId.DUMMY, 1L, 1);
    }

    @Override
    @NotNull
    public String getName() {
        return this.id;
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {

    }
}
