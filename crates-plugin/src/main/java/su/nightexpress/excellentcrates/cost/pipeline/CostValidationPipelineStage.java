package su.nightexpress.excellentcrates.cost.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.pipeline.CostPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.cost.core.CostService;

@NullMarked
public class CostValidationPipelineStage implements PipelineStage {

    private final CostService       costService;
    private final MessageDispatcher dispatcher;

    public CostValidationPipelineStage(CostService costService, MessageDispatcher dispatcher) {
        this.costService = costService;
        this.dispatcher = dispatcher;
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

        ActionResult result = this.costService.checkAffordance(player, crate, costTypeId, costOptionId, batchAmount);
        if (!result.success()) {
            result.handleFeedback((locale, ctx) -> this.dispatcher.send(player, locale, ctx));
            chain.abort();
            return;
        }

        chain.proceed(player, context);
    }
}
