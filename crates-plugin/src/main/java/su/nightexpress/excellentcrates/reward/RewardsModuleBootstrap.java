package su.nightexpress.excellentcrates.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelinePhase;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStageOrder;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantOrder;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaOrder;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.broadcast.RewardBroadcastBootstrapContext;
import su.nightexpress.excellentcrates.reward.command.RewardCommandBootstrapContext;
import su.nightexpress.excellentcrates.reward.config.RewardsConfigBootstrapContext;
import su.nightexpress.excellentcrates.reward.crate.component.RewardComponentBootstrapContext;
import su.nightexpress.excellentcrates.reward.data.RewardDataBootstrapContext;
import su.nightexpress.excellentcrates.reward.dispatcher.DefaultRewardMessageDispatcher;
import su.nightexpress.excellentcrates.reward.editor.RewardEditorBootstrapContext;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationBootstrapContext;
import su.nightexpress.excellentcrates.reward.feature.commands.RewardCommandsBootstrapContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.RewardCooldownBootstrapContext;
import su.nightexpress.excellentcrates.reward.feature.limit.RewardLimitsBootstrapContext;
import su.nightexpress.excellentcrates.reward.grant.RewardGrantBootstrapContext;
import su.nightexpress.excellentcrates.reward.items.RewardItemsBootstrapContext;
import su.nightexpress.excellentcrates.reward.placeholder.RewardPlaceholderService;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewBootstrapContext;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaBootstapContext;
import su.nightexpress.excellentcrates.reward.registry.DefaultRewardRegistry;
import su.nightexpress.excellentcrates.reward.selectable.SelectiveCoreBootstrapContext;
import su.nightexpress.excellentcrates.reward.selectable.SelectiveUIBootstrapContext;
import su.nightexpress.excellentcrates.reward.selectable.component.SelectiveComponentBootstrapContext;

@NullMarked
public class RewardsModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        DatabaseClient databaseClient = dependencies.databaseClient();

        CrateRegistry crates = dependencies.crateRegistry();
        CratePlaceholders cratePlaceholders = dependencies.cratePlaceholders();

        RewardRegistry rewards = new DefaultRewardRegistry();
        RewardPlaceholders rewardPlaceholders = new RewardPlaceholderService();
        RewardMessageDispatcher dispatcher = new DefaultRewardMessageDispatcher(
            dependencies.dispatcher(), rewardPlaceholders
        );

        RewardsConfigBootstrapContext configContext = new RewardsConfigBootstrapContext(plugin);

        // ----------------------------
        // Initialize Core Services
        // ----------------------------

        RewardPreviewBootstrapContext previewContext = new RewardPreviewBootstrapContext(
            plugin, rewardPlaceholders
        );
        RewardCommandBootstrapContext commandsContext = new RewardCommandBootstrapContext(
            plugin, configContext.settings
        );
        RewardDataBootstrapContext dataContext = new RewardDataBootstrapContext(
            plugin, rewards, configContext.settings
        );
        RewardQuotaBootstapContext quotaContext = new RewardQuotaBootstapContext();

        RewardGrantBootstrapContext grantContext = new RewardGrantBootstrapContext(
            cratePlaceholders, rewardPlaceholders, dispatcher
        );

        RewardEvaluationBootstrapContext evaluationContext = new RewardEvaluationBootstrapContext(
            rewards, dispatcher, quotaContext.quotaService
        );

        rewardPlaceholders.registerPlaceholder(evaluationContext.getPlaceholder());
        rewardPlaceholders.registerPlaceholder(previewContext.getPlaceholder());
        rewardPlaceholders.registerPlaceholder(quotaContext.getPlaceholder());

        dataContext.extensions.register(previewContext.getDataExtension());

        DefaultRewardsAPI.Builder apiBuilder = new DefaultRewardsAPI.Builder()
            .setDispatcher(dispatcher)
            .setRegistry(rewards)
            .setPlaceholders(rewardPlaceholders)
            .setCommands(commandsContext.api)
            .setData(dataContext.api)
            .setView(previewContext.api)
            .setEvaluator(evaluationContext.evaluationService)
            .setGrant(grantContext.api)
            .setQuota(quotaContext.api);

        this.registerComponent(configContext);
        this.registerComponent(previewContext);
        this.registerComponent(commandsContext);
        this.registerComponent(dataContext);

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.pipeline().registerExecutor(grantContext.getPipelineExecutor());
            cratesApi.pipeline().registerExecutor(quotaContext.getPipelineExecutor());
        });

        // ----------------------------
        // Initialize Editor
        // ----------------------------

        RewardEditorBootstrapContext editorContext = new RewardEditorBootstrapContext(
            plugin,
            coreUI,
            cratePlaceholders,
            dispatcher,
            rewards,
            dataContext.dataService,
            rewardPlaceholders,
            previewContext.previewService
        );

        apiBuilder.setEditor(editorContext.api);

        this.registerComponent(editorContext);

        // ----------------------------
        // Broadcast Bootstrap Context
        // ----------------------------
        if (configContext.modules.broadcastEnabled()) {
            RewardBroadcastBootstrapContext broadcastContext = new RewardBroadcastBootstrapContext(
                plugin, coreUI, dispatcher, rewards
            );

            dataContext.extensions.register(broadcastContext.getDataExtension());
            editorContext.extensions.register(broadcastContext.getEditorExtension());
            grantContext.api.registerProcessor(
                RewardGrantOrder.BROADCAST,
                broadcastContext.getGrantProcessor()
            );

            this.registerComponent(broadcastContext);
        }

        // ----------------------------
        // Initialize Reward Component Bootstrap Context
        // ----------------------------
        RewardComponentBootstrapContext componentContext = new RewardComponentBootstrapContext(
            plugin, coreUI, dispatcher, crates, rewards, dataContext.dataService, editorContext.uiService
        );

        this.registerComponent(componentContext);

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.pipeline().registerStage(
                PipelinePhase.PRE_ROLL,
                PipelineStageOrder.PRE_ROLL_REWARD_VALIDATION,
                evaluationContext.getValidationPipelineStage()
            );
            cratesApi.pipeline().registerStage(
                PipelinePhase.ROLL,
                PipelineStageOrder.ROLL_REWARDS,
                evaluationContext.getEvaluationPipelineStage()
            );
            cratesApi.data().registerExtension(componentContext.getCrateDataExtension());
            cratesApi.editor().registerExtension(componentContext.getCrateEditorExtension());
            cratesApi.getPlaceholders().registerPlaceholder(componentContext.getCratePlaceholder());
        });

        // ----------------------------
        // Initialize Commands Feature
        // ----------------------------
        if (configContext.modules.commandsEnabled()) {
            RewardCommandsBootstrapContext commandsContentContext = new RewardCommandsBootstrapContext(
                plugin, coreUI, dispatcher, rewards
            );

            dataContext.extensions.register(commandsContentContext.getDataExtension());
            editorContext.extensions.register(commandsContentContext.getEditorExtension());
            grantContext.api.registerProcessor(
                RewardGrantOrder.COMMANDS,
                commandsContentContext.getGrantProcessor()
            );

            this.registerComponent(commandsContentContext);
        }

        // ----------------------------
        // Reward Items Bootstrap Context
        // ----------------------------
        if (configContext.modules.itemsEnabled()) {
            RewardItemsBootstrapContext itemsContext = new RewardItemsBootstrapContext(
                plugin, coreUI, dispatcher, rewards
            );

            dataContext.extensions.register(itemsContext.getDataExtension());
            editorContext.extensions.register(itemsContext.getEditorExtension());
            grantContext.api.registerProcessor(
                RewardGrantOrder.ITEMS,
                itemsContext.getGrantProcessor()
            );

            this.registerComponent(itemsContext);
        }

        // ----------------------------
        // Initialize Cooldowns Feature
        // ----------------------------
        if (configContext.modules.cooldownsEnabled()) {
            RewardCooldownBootstrapContext cooldownsContext = new RewardCooldownBootstrapContext(
                plugin, databaseClient, coreUI, dispatcher, rewards
            );

            editorContext.extensions.register(cooldownsContext.getEditorExtension());
            dataContext.extensions.register(cooldownsContext.getDataExtension());
            rewardPlaceholders.registerPlaceholder(cooldownsContext.getRewardPlaceholder());
            quotaContext.api.registerProcessor(RewardQuotaOrder.COOLDOWN, cooldownsContext.getQuotaProcessor());

            this.registerComponent(cooldownsContext);
        }

        // ----------------------------
        // Initialize Limits Feature
        // ----------------------------
        if (configContext.modules.limitsEnabled()) {
            RewardLimitsBootstrapContext limitsContext = new RewardLimitsBootstrapContext(
                plugin, databaseClient, coreUI, dispatcher, rewards, rewardPlaceholders, previewContext.previewService
            );

            editorContext.extensions.register(limitsContext.getEditorExtension());
            dataContext.extensions.register(limitsContext.getDataExtension());
            quotaContext.api.registerProcessor(RewardQuotaOrder.LIMIT, limitsContext.getQuotaProcessor());
            rewardPlaceholders.registerPlaceholder(limitsContext.getPlaceholder());

            this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
                cratesApi.pipeline().registerStage(
                    PipelinePhase.MODIFICATION,
                    PipelineStageOrder.MODIFICATION_REWARD_LIMITS,
                    limitsContext.getPipelineStage()
                );
            });

            this.registerComponent(limitsContext);
        }

        // ---------------------------------
        // Selective Feature Bootstrap
        // ---------------------------------
        if (configContext.modules.selectionEnabled()) {
            SelectiveCoreBootstrapContext selectiveCoreContext = new SelectiveCoreBootstrapContext(
                plugin, rewards, quotaContext.quotaService
            );
            SelectiveComponentBootstrapContext selectiveComponentContext = new SelectiveComponentBootstrapContext(
                plugin, coreUI, dispatcher, crates
            );
            SelectiveUIBootstrapContext selectiveUIContext = new SelectiveUIBootstrapContext(
                plugin, coreUI, crates, dispatcher,
                rewardPlaceholders,
                previewContext.previewService,
                selectiveCoreContext.pickService
            );

            this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
                cratesApi.data().registerExtension(selectiveComponentContext.getDataExtension());
                cratesApi.editor().registerExtension(selectiveComponentContext.getEditorExtension());
                cratesApi.pipeline().registerStage(
                    PipelinePhase.ROLL,
                    PipelineStageOrder.ROLL_SELECTION,
                    selectiveUIContext.getPipelineStage()
                );
            });

            this.registerComponent(selectiveComponentContext);
            this.registerComponent(selectiveUIContext);
        }

        // ======================
        // Register the Reward System API
        // ======================

        services.register(CoreServices.REWARDS, apiBuilder.build());
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new RewardsModule();
    }
}
