package su.nightexpress.excellentcrates.reward.feature.cooldown.db.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownCachedDataService;

@NullMarked
public class RewardCooldownSaveController extends BaseController {

    private final RewardCooldownCachedDataService dataService;
    private final int                             dataSaveInterval;

    public RewardCooldownSaveController(CratesPlugin plugin,
                                        RewardCooldownCachedDataService dataService,
                                        int dataSaveInterval) {
        super(plugin);
        this.dataService = dataService;
        this.dataSaveInterval = dataSaveInterval;
    }

    @Override
    protected void onControllerReload() {
        this.stopTasks();
        this.runSaveTask();
    }

    @Override
    protected void onControllerShutdown() {
        this.dataService.saveDirtyData();
    }

    @Override
    protected void onControllerStart() {
        this.runSaveTask();
    }

    private void runSaveTask() {
        this.addAsyncSecondsTask(this.dataService::saveDirtyData, this.dataSaveInterval);
    }
}
