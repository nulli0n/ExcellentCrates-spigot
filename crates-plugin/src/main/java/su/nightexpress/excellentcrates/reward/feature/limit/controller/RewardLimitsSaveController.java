package su.nightexpress.excellentcrates.reward.feature.limit.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitSettings;

@NullMarked
public class RewardLimitsSaveController extends BaseController {

    private final RewardLimitCachedDataService          dataService;
    private final ReadOnlySettings<RewardLimitSettings> settings;

    public RewardLimitsSaveController(CratesPlugin plugin,
                                      RewardLimitCachedDataService dataService,
                                      ReadOnlySettings<RewardLimitSettings> settings) {
        super(plugin);
        this.dataService = dataService;
        this.settings = settings;
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
        this.addAsyncSecondsTask(this.dataService::saveDirtyData, this.settings.get().dataSaveInterval());
    }
}
