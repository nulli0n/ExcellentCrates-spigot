package su.nightexpress.excellentcrates.keys.storage;

import java.time.Duration;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.config.settings.KeyStorageSettings;
import su.nightexpress.excellentcrates.keys.storage.controller.KeyStorageDataSaveController;
import su.nightexpress.excellentcrates.keys.storage.controller.KeyStoragePlayerSessionController;
import su.nightexpress.excellentcrates.keys.storage.controller.KeyStorageRepositoryInitializer;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageSQLRepository;

@NullMarked
public final class KeyStorageBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.storage");
    private static final String     NAME = "Keys Storage";

    public final KeyStorageCachedDataService storageService;

    public KeyStorageBootstrapContext(CratesPlugin plugin,
                                      DatabaseClient databaseClient,
                                      ReadOnlySettings<KeyCoreSettings> settings) {
        super(ID, NAME);

        KeyStorageSettings storageSettings = settings.get().storage();
        Duration cacheTTL = Duration.ofMinutes(storageSettings.dataCacheTTL());

        KeyStorageSQLRepository repository = new KeyStorageSQLRepository(databaseClient, storageSettings.tableName());
        this.storageService = new KeyStorageCachedDataService(repository, cacheTTL);

        this.addComponent(new KeyStorageRepositoryInitializer(repository, this.storageService));
        this.addComponent(new KeyStoragePlayerSessionController(plugin, this.storageService));
        this.addComponent(new KeyStorageDataSaveController(plugin, this.storageService, settings));
    }
}
