package su.nightexpress.excellentcrates.crates.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.crates.config.settings.CrateCoreSettings;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;

@NullMarked
public class CrateDataSaveController extends BaseController {

    private final CrateDataService                    dataService;
    private final ReadOnlySettings<CrateCoreSettings> settings;

    public CrateDataSaveController(CratesPlugin plugin,
                                   CrateDataService dataService,
                                   ReadOnlySettings<CrateCoreSettings> settings) {
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
        this.save();
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
