package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class AtomEffect extends CrateEffect {

    private static final double RADIUS = 1.3;
    private static final int POINTS = 20;
    private static final int RINGS = 3;
    private static final double SPEED = 2.5;

    public AtomEffect() {
        super(EffectId.ATOM, 1L, 180);
    }

    @Override
    @NotNull
    public String getName() {
        return "Atom";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        double animationAngle = Math.toRadians(step * SPEED);
        for (int ringIndex = 0; ringIndex < RINGS; ringIndex++) {
            double ringOffset = Math.toRadians(ringIndex * (180.0 / RINGS));
            double totalAngle = animationAngle + ringOffset;
            for (int i = 0; i < POINTS; i++) {
                double theta = ((double) i / POINTS) * Math.PI * 2;
                double x = RADIUS * Math.cos(theta);
                double y = RADIUS * Math.sin(theta);
                double z = 0;
                Vector vec = new Vector(x, y, z);
                rotateAroundY(vec, totalAngle);
                Location loc = origin.clone().add(vec.getX(), vec.getY() + 0.5, vec.getZ());

                particle.play(player, loc, 0f, 0f, 1);
            }
        }
    }

    private void rotateAroundY(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        vector.setX(x).setZ(z);
    }
}