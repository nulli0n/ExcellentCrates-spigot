package su.nightexpress.excellentcrates.cost.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.pipeline.CostPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.cost.core.CostService;

@NullMarked
public class CostTakePipelineStage implements PipelineStage, CrateFeedbackHandler {

    private final CostService            costService;
    private final CrateMessageDispatcher dispatcher;

    public CostTakePipelineStage(CostService costService,
                                 CrateMessageDispatcher dispatcher) {
        this.costService = costService;
        this.dispatcher = dispatcher;
    }

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        CostPipelineComponent component = context.getComponentOrNull(PipelineComponentKeys.COST);
        if (component == null) {
            chain.proceed(player, context);
            return;
        }

        Identifier costTypeId = component.getCostTypeId();
        String costOptionId = component.getCostOptionId();

        BatchPipelineComponent batchComponent = context.getComponentOrNull(PipelineComponentKeys.BATCH);
        int batchAmount = batchComponent != null ? batchComponent.getSelectedAmount() : 1;

        ActionResult result = this.costService.takeCost(player, crate, costTypeId, costOptionId, batchAmount);

        this.handleFeedbackBase(player, crate, result);

        if (result.success()) {
            chain.proceed(player, context);
        }
        else {
            chain.abort();
        }
    }
}
