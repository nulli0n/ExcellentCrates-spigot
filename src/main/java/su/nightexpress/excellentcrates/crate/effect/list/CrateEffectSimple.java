package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nexmedia.engine.utils.EffectUtil;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectTask;

public class CrateEffectSimple extends CrateEffectTask {

    public CrateEffectSimple() {
        super(CrateEffectModel.SIMPLE, 2L, 1);
    }

    @Override
    public void doStep(@NotNull Location loc, @NotNull SimpleParticle particle, int step) {
        particle.play(loc.clone().add(0, 0.5D, 0), 0.3f, 0.3f, 0.3f, 0.1f, 30);
    }
}
