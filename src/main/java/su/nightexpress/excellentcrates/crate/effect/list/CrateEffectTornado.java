package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EffectUtil;
import su.nightexpress.excellentcrates.crate.effect.AbstractCrateBlockEffect;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;

import java.util.ArrayList;
import java.util.List;

public class CrateEffectTornado extends AbstractCrateBlockEffect {

    private final double yOffset          = 0.15D;
    private final float  tornadoHeight    = 3.15F;
    private final float  maxTornadoRadius = 2.25F;
    private final double distance         = 0.375D;

    public CrateEffectTornado() {
        super(CrateEffectModel.TORNADO, 4L, 7);
    }

    @Override
    public void doStep(@NotNull Location loc2, @NotNull String particleName, @NotNull String particleData, int step) {
        Location loc = loc2.clone().add(0.0D, 0.5D, 0.0D);
        double offset = 0.25D * (this.maxTornadoRadius * (2.35D / this.tornadoHeight));
        double vertical = this.tornadoHeight - this.distance * step;

        double radius = offset * vertical;
        if (radius > this.maxTornadoRadius) {
            radius = this.maxTornadoRadius;
        }
        for (Vector vector : this.createCircle(vertical, radius)) {
            EffectUtil.playEffect(loc.add(vector), particleName, particleData, 0.1f, 0.1f, 0.1f, 0.0f, 3);
            loc.subtract(vector);
        }
        loc.subtract(0.0D, this.yOffset, 0.0D);
    }

    private List<Vector> createCircle(double vertical, double radius) {
        double amount = radius * 64.0D;
        double d2 = 6.283185307179586D / amount;
        List<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double d3 = i * d2;
            double cos = radius * Math.cos(d3);
            double sin = radius * Math.sin(d3);
            Vector vector = new Vector(cos, vertical, sin);
            vectors.add(vector);
        }
        return vectors;
    }
}
