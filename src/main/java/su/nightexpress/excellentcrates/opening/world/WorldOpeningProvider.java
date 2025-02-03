package su.nightexpress.excellentcrates.opening.world;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;

import java.io.File;

public abstract class WorldOpeningProvider extends AbstractFileData<CratesPlugin> implements OpeningProvider {

    public WorldOpeningProvider(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {


        return this.readAdditional(config);
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {

        this.writeAdditional(config);
    }

    protected abstract boolean readAdditional(@NotNull FileConfig config);

    protected abstract void writeAdditional(@NotNull FileConfig config);
}
