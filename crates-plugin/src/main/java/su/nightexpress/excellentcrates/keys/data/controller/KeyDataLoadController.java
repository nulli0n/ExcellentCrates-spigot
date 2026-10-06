package su.nightexpress.excellentcrates.keys.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.data.KeyDataService;

@NullMarked
public class KeyDataLoadController extends BasePluginComponent {

    private final KeyRegistry    registry;
    private final KeyDataService dataService;

    public KeyDataLoadController(KeyRegistry registry, KeyDataService dataService) {
        super();
        this.registry = registry;
        this.dataService = dataService;
    }

    @Override
    protected void onReload() {
        this.dataService.saveDirty();
        this.registry.clear();
        this.dataService.loadKeys();
    }

    @Override
    protected void onShutdown() {
        this.dataService.saveDirty();
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadKeys();
    }
}
