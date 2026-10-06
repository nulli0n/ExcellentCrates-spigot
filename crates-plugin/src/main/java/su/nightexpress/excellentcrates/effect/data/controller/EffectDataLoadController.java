package su.nightexpress.excellentcrates.effect.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.data.EffectDataService;

@NullMarked
public class EffectDataLoadController extends BasePluginComponent {

    private final EffectRegistry    registry;
    private final EffectDataService dataService;

    public EffectDataLoadController(EffectRegistry registry, EffectDataService dataService) {
        super();
        this.registry = registry;
        this.dataService = dataService;
    }

    @Override
    protected void onReload() {
        this.registry.clearProfiles();
        this.dataService.loadProfiles();
    }

    @Override
    protected void onShutdown() {
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadProfiles();
    }
}
