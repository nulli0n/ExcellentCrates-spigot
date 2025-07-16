package su.nightexpress.excellentcrates.api.opening;

import org.jetbrains.annotations.NotNull;

public class ProviderLoader {

    private final String directory;
    private final ProviderSupplier supplier;

    public ProviderLoader(@NotNull String directory, @NotNull ProviderSupplier supplier) {
        this.directory = directory;
        this.supplier = supplier;
    }

    @NotNull
    public String getDirectory() {
        return this.directory;
    }

    @NotNull
    public ProviderSupplier getSupplier() {
        return this.supplier;
    }
}
