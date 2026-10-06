package su.nightexpress.excellentcrates.keys.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.data.KeyDataService;

@NullMarked
public class KeyDataSaveController extends BaseController {

    private final KeyDataService                    dataService;
    private final ReadOnlySettings<KeyCoreSettings> settings;

    public KeyDataSaveController(CratesPlugin plugin, KeyDataService dataService,
                                 ReadOnlySettings<KeyCoreSettings> settings) {
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
