package su.nightexpress.excellentcrates.reward.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.config.settings.RewardCoreSettings;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;

@NullMarked
public class RewardDataSaveController extends BaseController {

    private final RewardDataService                    dataService;
    private final ReadOnlySettings<RewardCoreSettings> settings;

    public RewardDataSaveController(CratesPlugin plugin,
                                    RewardDataService dataService,
                                    ReadOnlySettings<RewardCoreSettings> settings) {
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

    }

    @Override
    protected void onControllerStart() {
        this.runSaveTask();
    }

    private void runSaveTask() {
        this.addAsyncSecondsTask(this::save, this.settings.get().dataSaveInterval());
    }

    private void save() {
        this.dataService.saveDirty();
    }
}
