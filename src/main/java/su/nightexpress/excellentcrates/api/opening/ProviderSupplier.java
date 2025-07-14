package su.nightexpress.excellentcrates.api.opening;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;

public interface ProviderSupplier {

    @NotNull OpeningProvider supply(@NotNull CratesPlugin plugin, /*@NotNull FileConfig config,*/ @NotNull String id);
}
