package su.nightexpress.excellentcrates.rarity.data.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.rarity.IRarityDataService;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;

@NullMarked
public class RarityDataLoader extends BasePluginComponent {

    private final IRarityDataService dataService;
    private final RarityRegistry     registry;

    public RarityDataLoader(IRarityDataService dataService, RarityRegistry registry) {
        super();
        this.dataService = dataService;
        this.registry = registry;
    }

    @Override
    protected void onReload() {
        this.dataService.saveDirty();
        this.dataService.loadRarities();
    }

    @Override
    protected void onShutdown() {
        this.dataService.saveDirty();
        this.dataService.unloadRarities();
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadRarities();
    }
}
