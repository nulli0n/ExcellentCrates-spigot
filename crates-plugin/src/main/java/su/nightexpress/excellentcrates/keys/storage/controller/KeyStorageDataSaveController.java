package su.nightexpress.excellentcrates.keys.storage.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;

@NullMarked
public class KeyStorageDataSaveController extends BaseController {

    private final KeyStorageCachedDataService       cacheService;
    private final ReadOnlySettings<KeyCoreSettings> settings;

    public KeyStorageDataSaveController(CratesPlugin plugin,
                                        KeyStorageCachedDataService cacheService,
                                        ReadOnlySettings<KeyCoreSettings> settings) {
        super(plugin);
        this.cacheService = cacheService;
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
        this.addAsyncSecondsTask(this.cacheService::saveDirtyData, this.settings.get().storage().dataSaveInterval());
    }
}
