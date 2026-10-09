package su.nightexpress.excellentcrates.crates;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelinePhase;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStageOrder;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.crates.batch.BatchBootstrapContext;
import su.nightexpress.excellentcrates.crates.block.BlockCoreBootstrapContext;
import su.nightexpress.excellentcrates.crates.block.component.BlockComponentBootstrapContext;
import su.nightexpress.excellentcrates.crates.command.CrateCommandsContext;
import su.nightexpress.excellentcrates.crates.config.CratesConfigBootstrapContext;
import su.nightexpress.excellentcrates.crates.cooldown.CrateCooldownBootstrapContext;
import su.nightexpress.excellentcrates.crates.data.CrateDataContext;
import su.nightexpress.excellentcrates.crates.display.editor.DisplayEditorContext;
import su.nightexpress.excellentcrates.crates.editor.CrateEditorContext;
import su.nightexpress.excellentcrates.crates.history.CrateHistoryBootstrapContext;
import su.nightexpress.excellentcrates.crates.hologram.HologramBootstrapContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.HologramEditorContext;
import su.nightexpress.excellentcrates.crates.interact.InteractionBootstrapContext;
import su.nightexpress.excellentcrates.crates.item.CrateItemBootstrapContext;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.excellentcrates.crates.open.CrateOpenActionsBootstrapContext;
import su.nightexpress.excellentcrates.crates.pipeline.CratePipelineBootstrapContext;
import su.nightexpress.excellentcrates.crates.placeholder.CratePlaceholderAPIResolver;
import su.nightexpress.excellentcrates.crates.quota.CrateQuotaBootstrapContext;

@NullMarked
public final class CratesModuleBootstrap extends BaseModuleBootstrap {

    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        CrateMessageDispatcher dispatcher = dependencies.dispatcher();
        DatabaseClient databaseClient = dependencies.databaseClient();

        CrateRegistry crates = dependencies.crateRegistry();
        CratePlaceholders cratePlaceholders = dependencies.cratePlaceholders();

        plugin.injectLang(CratesLang.class);
        plugin.registerPermissions(CratePerms.ROOT);
        plugin.addPlaceholderAPIResolver(new CratePlaceholderAPIResolver(crates, cratePlaceholders));

        // -----------------------------------------
        // Initialize Core Systems
        // -----------------------------------------

        CratesConfigBootstrapContext configContext = new CratesConfigBootstrapContext(plugin);
        CrateDataContext dataContext = new CrateDataContext(plugin, crates, configContext.settings);
        CrateCommandsContext commandsContext = new CrateCommandsContext(plugin, crates, configContext.settings);
        CrateQuotaBootstrapContext quotaContext = new CrateQuotaBootstrapContext(dispatcher);
        CratePipelineBootstrapContext pipelineContext = new CratePipelineBootstrapContext(dispatcher);
        InteractionBootstrapContext interactionContext = new InteractionBootstrapContext(plugin);

        CrateItemBootstrapContext itemContext = new CrateItemBootstrapContext(
            plugin, coreUI, dispatcher, crates, interactionContext.interactionService
        );

        CrateEditorContext editorContext = new CrateEditorContext(
            plugin, coreUI, dispatcher, crates,
            itemContext.itemFactory, dataContext.dataService, cratePlaceholders
        );

        CrateOpenActionsBootstrapContext openingContext = new CrateOpenActionsBootstrapContext(
            plugin, coreUI, dispatcher, crates, cratePlaceholders
        );

        DisplayEditorContext displayEditorContext = new DisplayEditorContext(plugin, coreUI, dispatcher, crates);

        // -----------------------------------------
        // Register Core Systems
        // -----------------------------------------

        this.registerComponent(configContext);
        this.registerComponent(commandsContext);
        this.registerComponent(dataContext);
        this.registerComponent(quotaContext);
        this.registerComponent(pipelineContext);
        this.registerComponent(interactionContext);
        this.registerComponent(itemContext);
        this.registerComponent(editorContext);
        this.registerComponent(openingContext);
        this.registerComponent(displayEditorContext);

        // -----------------------------------------
        // Wire Core Systems Together
        // -----------------------------------------

        interactionContext.actions.register(pipelineContext.interactAction);

        commandsContext.commands.registerAll(itemContext.getCommands());
        commandsContext.commands.register(editorContext.editorCommand);
        commandsContext.commands.register(pipelineContext.crateCommand);

        editorContext.extensions.register(openingContext.getEditorExtension());
        editorContext.extensions.register(displayEditorContext.getEditorExtension());
        editorContext.extensions.register(itemContext.getEditorExtension());

        dataContext.extensions.register(openingContext.getDataExtension());

        pipelineContext.executors.register(openingContext.getPipelineExecutor());
        pipelineContext.processors.register(openingContext.getPipelineProcessor());

        pipelineContext.api.registerStage(
            PipelinePhase.PRE_ROLL,
            PipelineStageOrder.PRE_ROLL_CRATE_QUOTA,
            quotaContext.getTestPipelineStage()
        );

        pipelineContext.api.registerStage(
            PipelinePhase.PRE_PROCESS,
            PipelineStageOrder.PRE_PROCESS_CRATE_QUOTA,
            quotaContext.getApplyPipelineStage()
        );

        pipelineContext.api.registerStage(
            PipelinePhase.VALIDATION,
            PipelineStageOrder.VALIDATE_ITEM,
            itemContext.getValidationStage()
        );

        pipelineContext.api.registerStage(
            PipelinePhase.PRE_PROCESS,
            PipelineStageOrder.PRE_PROCESS_ITEM_TAKE,
            itemContext.getTakeStage()
        );

        // -----------------------------------------
        // Initialize API Builder
        // -----------------------------------------

        DefaultCratesAPI.Builder apiBuilder = DefaultCratesAPI.builder()
            .registry(crates)
            .placeholders(cratePlaceholders)
            .commands(commandsContext.api)
            .data(dataContext.api)
            .editor(editorContext.api)
            .items(itemContext.api)
            .pipeline(pipelineContext.api)
            .interaction(interactionContext.api);

        // -----------------------------------------
        // Crate History Bootstrap
        // -----------------------------------------

        if (configContext.modules.historyEnabled()) {
            CrateHistoryBootstrapContext crateHistoryContext = new CrateHistoryBootstrapContext(
                plugin.configPath(), plugin.logsPath()
            );
            this.registerComponent(crateHistoryContext);

            pipelineContext.executors.register(crateHistoryContext.getPipelineExecutor());
        }

        // -----------------------------------------
        // Batch System Bootstrap
        // -----------------------------------------

        if (configContext.modules.batchEnabled()) {
            BatchBootstrapContext batchContext = new BatchBootstrapContext(plugin, coreUI, dispatcher, crates);
            this.registerComponent(batchContext);

            pipelineContext.api.registerStage(
                PipelinePhase.BUILD,
                PipelineStageOrder.BUILD_BATCH,
                batchContext.getInitializePipelineStage()
            );

            pipelineContext.api.registerStage(
                PipelinePhase.PRE_ROLL,
                PipelineStageOrder.PRE_ROLL_BATCH_SELECTION,
                batchContext.getSelectionPipelineStage()
            );
        }

        // -----------------------------------------
        // Block System Bootstrap
        // -----------------------------------------

        if (configContext.modules.blocksEnabled()) {
            BlockCoreBootstrapContext blockCoreContext = new BlockCoreBootstrapContext(
                plugin, dispatcher, crates, cratePlaceholders,
                dataContext.dataService, interactionContext.interactionService
            );

            BlockComponentBootstrapContext blockComponentContext = new BlockComponentBootstrapContext(
                plugin, coreUI, dispatcher, crates, cratePlaceholders,
                blockCoreContext.positions, blockCoreContext.itemService
            );

            this.registerComponent(blockCoreContext);
            this.registerComponent(blockComponentContext);

            commandsContext.commands.registerAll(blockCoreContext.getCommands());
            dataContext.extensions.register(blockComponentContext.getDataExtension());
            editorContext.extensions.register(blockComponentContext.getEditorExtension());

            apiBuilder.blocks(blockCoreContext.api);

            // -----------------------------------------
            // Hologram System Bootstrap
            // -----------------------------------------

            if (configContext.modules.hologramsEnabled()) {
                HologramBootstrapContext hologramSystemContext = new HologramBootstrapContext(
                    plugin, crates, cratePlaceholders, blockCoreContext.positions
                );
                HologramEditorContext hologramEditorContext = new HologramEditorContext(
                    plugin, coreUI, dispatcher, crates,
                    hologramSystemContext.displayService, cratePlaceholders
                );

                apiBuilder.holograms(hologramSystemContext.api);

                dataContext.extensions.register(hologramSystemContext.getDataExtension());
                editorContext.extensions.register(hologramEditorContext.getEditorExtension());
                pipelineContext.executors.register(hologramSystemContext.getPipelineExecutor());
                pipelineContext.api.registerStage(
                    PipelinePhase.PRE_PROCESS,
                    PipelineStageOrder.PRE_PROCESS_HOLOGRAMS,
                    hologramSystemContext.getPipelineStage()
                );

                this.registerComponent(hologramSystemContext);
                this.registerComponent(hologramEditorContext);
            }
        }

        // -----------------------------------------
        // Cooldown System Bootstrap
        // -----------------------------------------

        if (configContext.modules.cooldownsEnabled()) {
            CrateCooldownBootstrapContext cooldownContext = new CrateCooldownBootstrapContext(
                plugin, databaseClient, coreUI, dispatcher, crates, cratePlaceholders
            );

            this.registerComponent(cooldownContext);

            editorContext.extensions.register(cooldownContext.getEditorExtension());
            dataContext.extensions.register(cooldownContext.getDataExtension());
            quotaContext.quotaProcessors.register(cooldownContext.getQuotaProcessor());
            cratePlaceholders.registerPlaceholder(cooldownContext.getCratePlaceholder());

            apiBuilder.cooldowns(cooldownContext.api);
        }

        // -----------------------------------------
        // API Injection
        // -----------------------------------------

        services.register(CoreServices.CRATES, apiBuilder.build());
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new CratesModule();
    }
}
