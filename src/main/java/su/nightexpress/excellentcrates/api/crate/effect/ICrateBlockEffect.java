package su.nightexpress.excellentcrates.api.crate.effect;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface ICrateBlockEffect {

    void run();

    void start();

    void doStep(@NotNull Location location, @NotNull String particleName, @NotNull String particleData, int step);

    long getInterval();

    int getDuration();
}
