package su.nightexpress.excellentcrates.reward.feature.cooldown.db.controller;

import java.util.Collections;
import java.util.concurrent.CompletionException;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.reward.feature.cooldown.RewardCooldownService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownSQLRepository;

@NullMarked
public class RewardCooldownDatabaseInitializer extends BasePluginComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardCooldownDatabaseInitializer.class);

    private final RewardCooldownSQLRepository     repository;
    private final RewardCooldownCachedDataService dataService;
    private final RewardCooldownService           cooldownService;

    public RewardCooldownDatabaseInitializer(RewardCooldownSQLRepository repository,
                                             RewardCooldownCachedDataService dataService,
                                             RewardCooldownService cooldownService) {
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
