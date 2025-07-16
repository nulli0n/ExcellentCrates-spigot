package su.nightexpress.excellentcrates.opening;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;

public abstract class AbstractProvider implements OpeningProvider {

    protected final CratesPlugin plugin;
    protected final String id;

    public AbstractProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        this.plugin = plugin;
        this.id = id;
    }

    @Override
    @NotNull
    public String getId() {
        return this.id;
    }
}
