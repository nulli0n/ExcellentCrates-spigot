package su.nightexpress.excellentcrates.crates.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.CrateDataAPI;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;

@NullMarked
public class DefaultCrateDataAPI implements CrateDataAPI {

    private final TinyRegistry<CrateDataExtension> extensions;
    private final CrateDataService                 dataService;

    public DefaultCrateDataAPI(TinyRegistry<CrateDataExtension> extensions, CrateDataService dataService) {
        this.extensions = extensions;
        this.dataService = dataService;
    }

    @Override
    public void markDirty(Crate crate) {
        this.dataService.markDirty(crate);
    }

    @Override
    public void registerExtension(CrateDataExtension extension) {
        this.extensions.register(extension);
    }
}
