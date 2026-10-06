package su.nightexpress.excellentcrates.crates.cooldown.db.controller;

import java.util.Collections;
import java.util.concurrent.CompletionException;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.crates.cooldown.CrateCooldownService;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownCachedDataService;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownSQLRepository;

@NullMarked
public class CrateCooldownDatabaseInitializer extends BasePluginComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateCooldownDatabaseInitializer.class);

    private final CrateCooldownSQLRepository     repository;
    private final CrateCooldownCachedDataService dataService;
    private final CrateCooldownService           cooldownService;

    public CrateCooldownDatabaseInitializer(CrateCooldownSQLRepository repository,
                                            CrateCooldownCachedDataService dataService,
                                            CrateCooldownService cooldownService) {
        super();
        this.repository = repository;
        this.dataService = dataService;
        this.cooldownService = cooldownService;
    }

    @Override
    protected void onReload() {
        this.cooldownService.loadGlobalCooldowns().exceptionally(exception -> {
            LOGGER.error("Failed to load global cooldowns", exception);
            return Collections.emptyList();
        });
    }

    @Override
    protected void onShutdown() {
        this.dataService.clearCache();
    }

    @Override
    protected void onStart() {
        repository.initTable();
        repository.registerCooldownSync(dataService);

        try {
            cooldownService.loadGlobalCooldowns().join();
        }
        catch (CompletionException exception) {
            LOGGER.error("Failed to load global cooldowns", exception);
        }
    }
}
