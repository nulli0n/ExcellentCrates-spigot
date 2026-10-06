package su.nightexpress.excellentcrates.cost;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostAPI;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelinePhase;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStageOrder;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.cost.core.CostService;
import su.nightexpress.excellentcrates.cost.lang.CostLang;
import su.nightexpress.excellentcrates.cost.pipeline.CostSelectionPipelineStage;
import su.nightexpress.excellentcrates.cost.pipeline.CostTakePipelineStage;
import su.nightexpress.excellentcrates.cost.pipeline.CostValidationPipelineStage;
import su.nightexpress.excellentcrates.cost.ui.CostUIBootstrapContext;

@NullMarked
public final class CostModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        CrateMessageDispatcher dispatcher = dependencies.dispatcher();

        CrateRegistry crates = dependencies.crateRegistry();

        plugin.injectLang(CostLang.class);

        IdentifiableRegistry<CostType<?>> costTypes = new IdentifiableRegistry<>();

        CostService costService = new CostService(costTypes);

        CostUIBootstrapContext uiContext = new CostUIBootstrapContext(
            plugin, coreUI, dispatcher, crates, costTypes, costService
        );
        this.registerComponent(uiContext);

        this.bridge.requireAvailable(CoreServices.CRATES, cratesAPI -> {
            cratesAPI.pipeline().registerStage(
                PipelinePhase.PRE_ROLL,
                PipelineStageOrder.PRE_ROLL_COST_SELECTION,
                new CostSelectionPipelineStage(uiContext.selectionHandler, costService)
            );
            cratesAPI.pipeline().registerStage(
                PipelinePhase.VALIDATION,
                PipelineStageOrder.VALIDATE_COST,
                new CostValidationPipelineStage(costService, dispatcher)
            );
            cratesAPI.pipeline().registerStage(
                PipelinePhase.PRE_PROCESS,
                PipelineStageOrder.PRE_PROCESS_COST_TAKE,
                new CostTakePipelineStage(costService, dispatcher)
            );
        });

        CostAPI api = new DefaultCostAPI(costTypes, costService);

        services.register(CoreServices.COST, api);
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new CostModule();
    }
}
