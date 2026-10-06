package su.nightexpress.excellentcrates.reward.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;

@NullMarked
public class RewardDataLoader extends BasePluginComponent {

    private final RewardDataService dataService;
    private final RewardRegistry    registry;

    public RewardDataLoader(RewardDataService dataService, RewardRegistry registry) {
        super();
        this.dataService = dataService;
        this.registry = registry;
    }

    @Override
    protected void onReload() {
        this.dataService.saveDirty();
        this.registry.clear();
        this.dataService.loadRewards();
    }

    @Override
    protected void onShutdown() {
        this.dataService.saveDirty();
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadRewards();
    }
}
