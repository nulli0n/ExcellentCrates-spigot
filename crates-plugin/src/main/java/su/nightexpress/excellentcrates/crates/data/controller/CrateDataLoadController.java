package su.nightexpress.excellentcrates.crates.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;

@NullMarked
public class CrateDataLoadController extends BasePluginComponent {

    private final CrateRegistry    registry;
    private final CrateDataService dataService;

    public CrateDataLoadController(CrateRegistry registry, CrateDataService dataService) {
        super();
        this.registry = registry;
        this.dataService = dataService;
    }

    @Override
    protected void onReload() {
        this.dataService.saveDirty();
        this.dataService.unloadCrates();
        this.registry.clear();
        this.dataService.loadCrates();
    }

    @Override
    protected void onShutdown() {
        this.dataService.saveDirty();
        this.dataService.unloadCrates();
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadCrates();
    }
}
