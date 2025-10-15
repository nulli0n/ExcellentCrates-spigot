package su.nightexpress.excellentcrates.api.cost;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface CostType {

    boolean isAvailable();

    @NotNull String getId();

    @NotNull String getName();

    @NotNull CostEntry load(@NotNull FileConfig config, @NotNull String path);

    @NotNull CostEntry createEmpty();
}
