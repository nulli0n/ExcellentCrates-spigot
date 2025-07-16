package su.nightexpress.excellentcrates.api.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;

public interface Opening {

    @Deprecated
    default void run() {
        this.start();
    }

    void start();

    void stop();

    void tick();

    boolean isCompleted();

    long getInterval();

    long getTickCount();

    boolean isTickTime();

    boolean isRunning();

    @NotNull Player getPlayer();

    @NotNull CrateSource getSource();

    @NotNull Crate getCrate();

    @Nullable CrateKey getKey();

    void instaRoll();

    boolean isRefundable();

    void setRefundable(boolean refundable);
}
