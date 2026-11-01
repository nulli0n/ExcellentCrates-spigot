package su.nightexpress.excellentcrates.opening.inventory.spinner;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Spinner;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.config.FileConfig;

public interface SpinnerProvider {

    void write(@NotNull FileConfig config, @NotNull String path);

    @NotNull Spinner createSpinner(@NotNull CratesPlugin plugin, @NotNull SpinnerData data, @NotNull InventoryOpening opening);
}
