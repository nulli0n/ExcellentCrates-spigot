package su.nightexpress.excellentcrates.keys.storage.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageSQLRepository;

@NullMarked
public class KeyStorageRepositoryInitializer implements StartupComponent {

    private final KeyStorageSQLRepository     repository;
    private final KeyStorageCachedDataService cacheService;

    public KeyStorageRepositoryInitializer(KeyStorageSQLRepository repository,
                                           KeyStorageCachedDataService cacheService) {
        this.repository = repository;
        this.cacheService = cacheService;
    }

    @Override
    public void start() {
        this.repository.initTable();
        this.repository.registerKeyDataSync(this.cacheService);
    }
}
