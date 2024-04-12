package su.nightexpress.excellentcrates.api.opening;

import org.jetbrains.annotations.NotNull;

public interface Spinner extends Tickable {

    @NotNull String getId();

    int getTotalSpins();

    long getCurrentSpins();

    void setCurrentSpins(long spins);

    default boolean hasSpin() {
        return this.getCurrentSpins() > 0L;
    }
}
