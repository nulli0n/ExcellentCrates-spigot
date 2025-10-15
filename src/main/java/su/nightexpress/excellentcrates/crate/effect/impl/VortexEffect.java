package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class VortexEffect extends CrateEffect {

    private static final int    STRANDS   = 2;
    private static final int    PARTICLES = 170 / 5;
    private static final float  RADIUS    = 1.5F;
    private static final float  CURVE     = 2.0F;
    private static final double ROTATION  = 0.7853981633974483D;

    public VortexEffect() {
        super(EffectId.VORTEX, 1L, PARTICLES);
    }

    @Override
    @NotNull
    public String getName() {
        return Lang.EFFECT_MODEL_VORTEX.text();
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        for (int boost = 0; boost < 3; boost++) {
            for (int strand = 1; strand <= STRANDS; ++strand) {
                float progress = step / (float) PARTICLES;
                double point = CURVE * progress * 2.0f * Math.PI / STRANDS + 2 * Math.PI * strand / STRANDS + ROTATION;
                double addX = Math.cos(point) * progress * RADIUS;
                double addZ = Math.sin(point) * progress * RADIUS;
                double addY = 3.5D - 0.02 * 5 * step;
                Location location = origin.clone().add(addX, addY, addZ);
                particle.play(player, location, 0.1f, 0.0f, 1);
            }
        }
    }
}
