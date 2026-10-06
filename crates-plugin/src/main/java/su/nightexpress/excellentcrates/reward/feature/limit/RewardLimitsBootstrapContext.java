package su.nightexpress.excellentcrates.reward.feature.limit;

import java.nio.file.Path;
import java.time.Duration;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.settings.FastSettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.reward.feature.limit.component.DefaultRewardLimitComponent;
import su.nightexpress.excellentcrates.reward.feature.limit.component.codec.RewardLimitComponentCodec;
import su.nightexpress.excellentcrates.reward.feature.limit.component.extension.RewardLimitsDataExtension;
import su.nightexpress.excellentcrates.reward.feature.limit.controller.RewardLimitsPlayerSessionController;
import su.nightexpress.excellentcrates.reward.feature.limit.controller.RewardLimitsRepositoryInitializer;
import su.nightexpress.excellentcrates.reward.feature.limit.controller.RewardLimitsSaveController;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitSQLRepository;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitSettings;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.RewardLimitsEditorContext;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;
import su.nightexpress.excellentcrates.reward.feature.limit.pipeline.RewardLimitsAlternativePipelineStage;
import su.nightexpress.excellentcrates.reward.feature.limit.placeholder.RewardLimitsPlaceholder;
import su.nightexpress.excellentcrates.reward.feature.limit.quota.RewardLimitsQuotaProcessor;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardLimitsBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.limits");
    private static final String     NAME = "Limits";

    private static final String SETTINGS_FILE_NAME = "rewards.limits.yml";

    private final RewardDataExtension   dataExtension;
    private final RewardQuotaProcessor  quotaProcessor;
    private final RewardEditorExtension editorExtension;
    private final RewardPlaceholder     placeholder;
    private final PipelineStage         pipelineStage;

    public RewardLimitsBootstrapContext(CratesPlugin plugin,
                                        DatabaseClient databaseClient,
                                        CoreUIService coreUI,
                                        RewardMessageDispatcher dispatcher,
                                        RewardRegistry registry,
                                        RewardPlaceholders rewardPlaceholders,
                                        RewardPreviewService viewService) {
        super(ID, NAME);

        plugin.injectLang(RewardLimitsLang.class);

        ConfigCodecs.register(DefaultRewardLimitComponent.class, RewardLimitComponentCodec.INSTANCE);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<RewardLimitSettings> settings = new SettingsProvider<>(RewardLimitSettings.defaults());

        this.addComponent(FastSettingsController.createAndLoad(settingsPath, RewardLimitSettings::loadFrom, settings));

        Duration cacheTTL = Duration.ofMinutes(settings.get().cacheTTL());
        String tableName = settings.get().tableName();

        RewardLimitSQLRepository repository = new RewardLimitSQLRepository(databaseClient, tableName);
        RewardLimitCachedDataService dataService = new RewardLimitCachedDataService(repository, cacheTTL);
        RewardLimitManageService manageService = new RewardLimitManageService(dataService);

        RewardLimitsEditorContext editorContext = new RewardLimitsEditorContext(
            plugin, coreUI, dispatcher, registry, viewService, rewardPlaceholders
        );

        this.dataExtension = new RewardLimitsDataExtension();
        this.editorExtension = editorContext.editorExtension;
        this.quotaProcessor = new RewardLimitsQuotaProcessor(manageService);
        this.pipelineStage = new RewardLimitsAlternativePipelineStage(registry, manageService, dispatcher);
        this.placeholder = new RewardLimitsPlaceholder(manageService);

        this.addComponent(new RewardLimitsRepositoryInitializer(repository, dataService, manageService));
        this.addComponent(new RewardLimitsPlayerSessionController(plugin, dataService));
        this.addComponent(new RewardLimitsSaveController(plugin, dataService, settings));
        this.addComponent(editorContext);
    }

    public RewardDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public RewardEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public RewardQuotaProcessor getQuotaProcessor() {
        return this.quotaProcessor;
    }

    public RewardPlaceholder getPlaceholder() {
        return this.placeholder;
    }

    public PipelineStage getPipelineStage() {
        return this.pipelineStage;
    }
}
