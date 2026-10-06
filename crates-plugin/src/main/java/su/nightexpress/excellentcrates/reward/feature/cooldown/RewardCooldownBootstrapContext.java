package su.nightexpress.excellentcrates.reward.feature.cooldown;

import java.nio.file.Path;
import java.time.Duration;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownsAPI;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.cooldown.component.StandardRewardCooldownComponent;
import su.nightexpress.excellentcrates.reward.feature.cooldown.component.codec.RewardCooldownsCodec;
import su.nightexpress.excellentcrates.reward.feature.cooldown.component.extension.RewardCooldownsDataExtension;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownDBSettings;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownSQLRepository;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.controller.RewardCooldownDatabaseInitializer;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.controller.RewardCooldownPlayerSessionController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.controller.RewardCooldownSaveController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.RewardCooldownsEditorContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.lang.RewardCooldownsLang;
import su.nightexpress.excellentcrates.reward.feature.cooldown.placeholder.RewardCooldownPlaceholder;
import su.nightexpress.excellentcrates.reward.feature.cooldown.quota.RewardCooldownsQuotaProcessor;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class RewardCooldownBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.cooldown");
    private static final String     NAME = "Cooldowns";

    private static final String SETTINGS_FILE = "rewards.cooldowns.yml";

    public final RewardCooldownService cooldownService;
    public final RewardCooldownsAPI    api;

    private final RewardCooldownsEditorContext editorContext;

    private final RewardDataExtension  dataExtension;
    private final RewardQuotaProcessor quotaProcessor;
    private final RewardPlaceholder    rewardPlaceholder;

    public RewardCooldownBootstrapContext(CratesPlugin plugin,
                                          DatabaseClient databaseClient,
                                          CoreUIService coreUI,
                                          RewardMessageDispatcher dispatcher,
                                          RewardRegistry rewards) {
        super(ID, NAME);
        plugin.injectLang(RewardCooldownsLang.class);

        ConfigCodecs.register(StandardRewardCooldownComponent.class, RewardCooldownsCodec.INSTANCE);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE);
        RewardCooldownDBSettings dbSettings = RewardCooldownDBSettings.loadFrom(settingsPath);

        Duration cacheTTL = Duration.ofMinutes(dbSettings.cacheTTL());

        RewardCooldownSQLRepository repository = new RewardCooldownSQLRepository(databaseClient, dbSettings);
        RewardCooldownCachedDataService cacheService = new RewardCooldownCachedDataService(repository, cacheTTL);
        this.cooldownService = new RewardCooldownService(cacheService);

        this.editorContext = new RewardCooldownsEditorContext(plugin, coreUI, dispatcher, rewards);

        this.api = new DefaultRewardCooldownsAPI(this.cooldownService);

        this.dataExtension = new RewardCooldownsDataExtension(cacheService);
        this.quotaProcessor = new RewardCooldownsQuotaProcessor(this.cooldownService);
        this.rewardPlaceholder = new RewardCooldownPlaceholder(cooldownService);

        this.addComponent(new RewardCooldownDatabaseInitializer(repository, cacheService, this.cooldownService));
        this.addComponent(new RewardCooldownSaveController(plugin, cacheService, dbSettings.dataSaveInterval()));
        this.addComponent(new RewardCooldownPlayerSessionController(plugin, cacheService));
        this.addComponent(this.editorContext);
    }

    public RewardDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public RewardEditorExtension getEditorExtension() {
        return this.editorContext.editorExtension;
    }

    public RewardQuotaProcessor getQuotaProcessor() {
        return this.quotaProcessor;
    }

    public RewardPlaceholder getRewardPlaceholder() {
        return this.rewardPlaceholder;
    }
}
