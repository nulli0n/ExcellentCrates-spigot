package su.nightexpress.excellentcrates.reward.feature.limit.controller;

import java.util.Collections;
import java.util.concurrent.CompletionException;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.reward.feature.limit.RewardLimitManageService;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitSQLRepository;

@NullMarked
public class RewardLimitsRepositoryInitializer extends BasePluginComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardLimitsRepositoryInitializer.class);

    private final RewardLimitSQLRepository     repository;
    private final RewardLimitCachedDataService dataService;
    private final RewardLimitManageService     manageService;

    public RewardLimitsRepositoryInitializer(RewardLimitSQLRepository repository,
                                             RewardLimitCachedDataService dataService,
                                             RewardLimitManageService manageService) {
        super();
        this.repository = repository;
        this.dataService = dataService;
        this.manageService = manageService;
    }

    @Override
    protected void onReload() {
        this.manageService.loadGlobalLimits().exceptionally(exception -> {
            LOGGER.error("Failed to load global limits", exception);
            return Collections.emptyList();
        });
    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {
        this.repository.initTable();
        this.repository.registerSync(dataService);

        try {
            this.manageService.loadGlobalLimits().join();
        }
        catch (CompletionException exception) {
            LOGGER.error("Failed to load global limits", exception);
        }
    }
}
