package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.excellentcrates.util.pos.Point3D;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class HeartEffect extends CrateEffect {

    private static final int POINTS = 20;

    private boolean rotate = false;

    public HeartEffect() {
        super(EffectId.HEART, 1L, POINTS);
    }

    @Override
    @NotNull
    public String getName() {
        return Lang.EFFECT_MODEL_HEART.text();
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        boolean isX = this.rotate;
        origin = origin.add(0, 4, 0);

        double delta = Math.PI / POINTS;
        double angle = delta * step;
        double z = 16 * Math.pow(Math.sin(angle), 3);
        double y = 13 * Math.cos(angle) - 5 * Math.cos(2 * angle) - 2 * Math.cos(3 * angle) - Math.cos(4 * angle);
        double x = 0;

        double realX = isX ? z : x;
        double realZ = isX ? x : z;
        Point3D point = new Point3D(realX / 25, (y / 25) - 1.8, realZ / 25);
        Point3D mirrored = new Point3D(isX ? -point.x : point.x, point.y, isX ? point.z : -point.z);

        Location left = origin.clone().add(point.x, point.y, point.z);
        Location right = origin.clone().add(mirrored.x, mirrored.y, mirrored.z);

        particle.play(player, left, 0, 1);
        particle.play(player, right, 0, 1);

        if (step == 0) {
            this.rotate = !this.rotate;
        }
    }
}
