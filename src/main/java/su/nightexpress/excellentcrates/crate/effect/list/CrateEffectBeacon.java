package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectTask;

public class CrateEffectBeacon extends CrateEffectTask {

    public CrateEffectBeacon() {
        super(CrateEffectModel.BEACON, 3L, 40);
    }

    @Override
    public void doStep(@NotNull Location loc, @NotNull SimpleParticle particle, int step) {
        double n2 = 0.8975979010256552 * step;
        for (int i = step; i > Math.max(0, step - 25); --i) {
            particle.play(LocationUtil.getPointOnCircle(loc, n2, 0.55, i * 0.75), 0.0f, 0.15f, 0.0f, 0.0f, 4);
        }
    }
}
