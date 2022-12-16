package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EffectUtil;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectTask;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;

public class CrateEffectPulsar extends CrateEffectTask {

    public CrateEffectPulsar() {
        super(CrateEffectModel.PULSAR, 2L, 38);
    }

    @Override
    public void doStep(@NotNull Location loc2, @NotNull String particleName, @NotNull String particleData, int step) {
        Location loc = loc2.clone().add(0, -0.8D, 0);
        double n2 = (0.5 + step * 0.15) % 3.0;
        for (int n3 = 0; n3 < n2 * 10.0; ++n3) {
            double n4 = 6.283185307179586 / (n2 * 10.0) * n3;
            EffectUtil.playEffect(LocationUtil.getPointOnCircle(loc.clone(), false, n4, n2, 1.0), particleName, particleData, 0.1f, 0.1f, 0.1f, 0.0f, 2);
        }
    }
}
