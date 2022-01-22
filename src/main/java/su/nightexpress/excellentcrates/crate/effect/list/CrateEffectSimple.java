package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EffectUtil;
import su.nightexpress.excellentcrates.crate.effect.AbstractCrateBlockEffect;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;

public class CrateEffectSimple extends AbstractCrateBlockEffect {

    public CrateEffectSimple() {
        super(CrateEffectModel.SIMPLE, 2L, 1);
    }

    @Override
    public void doStep(@NotNull Location loc, @NotNull String particleName, @NotNull String particleData, int step) {
        EffectUtil.playEffect(loc.clone().add(0, 0.5D, 0), particleName, particleData, 0.3f, 0.3f, 0.3f, 0.1f, 30);
    }
}
