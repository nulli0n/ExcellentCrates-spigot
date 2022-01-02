package su.nightexpress.excellentcrates.crate.effect.list;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EffectUtil;
import su.nightexpress.excellentcrates.crate.effect.AbstractCrateBlockEffect;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectModel;

public class CrateEffectVortex extends AbstractCrateBlockEffect {

    private int strands = 2;
    private int particles = 170;
    private float radius = 1.5F;
    private float curve = 2.0F;
    private double rotation = 0.7853981633974483D;

    public CrateEffectVortex() {
        super(CrateEffectModel.VORTEX, 1L, 170);
    }

    @Override
    public void doStep(@NotNull Location loc, @NotNull String particleName, @NotNull String particleData, int step) {
        for (int boost = 0; boost < 5; boost++) {
            for (int strand = 1; strand <= this.strands; ++strand) {
                float progress = step / (float) this.particles;
                double point = this.curve * progress * 2.0f * Math.PI / this.strands + 6.283185307179586 * strand / this.strands + this.rotation;
                double addX = Math.cos(point) * progress * this.radius;
                double addZ = Math.sin(point) * progress * this.radius;
                double addY = 3.5D - 0.02 * step;
                Location location = loc.clone().add(addX, addY, addZ);
                EffectUtil.playEffect(location, particleName, particleData, 0.1f, 0.1f, 0.1f, 0.0f, 1);
            }
            step++;
        }
        this.step = step;
    }
}
