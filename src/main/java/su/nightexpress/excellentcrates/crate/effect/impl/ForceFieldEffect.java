package su.nightexpress.excellentcrates.crate.effect.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class ForceFieldEffect extends CrateEffect {

    // SETTINGS
    private static final double RADIUS = 1.3;        // Width of the cage
    private static final double HEIGHT = 2.5;        // Height of the cage
    private static final int STRANDS = 10;           // Number of vertical lines
    private static final double ROTATION_SPEED = 2.0; // How fast it spins sideways
    private static final double VERTICAL_SPEED = 0.1; // How fast particles flow upwards
    private static final double DENSITY = 0.2;       // Gap between particles in the line

    public ForceFieldEffect() {
        // ID, Delay (1 tick), Max Steps (360)
        super(EffectId.FORCE_FIELD, 1L, 360);
    }

    @Override
    @NotNull
    public String getName() {
        return "Force Field";
    }

    @Override
    public void onStepPlay(@NotNull Location origin, @NotNull UniParticle particle, int step, @NotNull Player player) {
        // 1. Calculate the rotation for this tick
        double rotation = Math.toRadians(step * ROTATION_SPEED);

        // 2. Calculate the vertical flow offset (0.0 to DENSITY)
        // This makes the particles look like they are sliding up the line endlessly
        double yOffset = (step * VERTICAL_SPEED) % DENSITY;

        // 3. Draw each vertical strand
        for (int i = 0; i < STRANDS; i++) {
            // Angle for this specific strand
            double angle = rotation + Math.toRadians(i * (360.0 / STRANDS));

            double x = Math.cos(angle) * RADIUS;
            double z = Math.sin(angle) * RADIUS;

            // 4. Draw the line from bottom to top
            // We start at 'yOffset' so the whole line texture moves up smoothly
            for (double y = yOffset; y <= HEIGHT; y += DENSITY) {
                // Add 0.2 to start slightly above ground
                Location loc = origin.clone().add(x, 0.2 + y, z);

                particle.play(player, loc, 0, 0, 0);
            }
        }
    }
}