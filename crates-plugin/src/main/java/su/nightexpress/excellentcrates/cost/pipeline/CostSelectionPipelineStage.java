package su.nightexpress.excellentcrates.cost.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.CostSelectionHandler;
import su.nightexpress.excellentcrates.api.cost.pipeline.CostPipelineComponent;
import su.nightexpress.excellentcrates.api.cost.type.CostTypeOption;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.cost.core.CostService;

@NullMarked
public class CostSelectionPipelineStage implements PipelineStage {

    private final CostSelectionHandler selectionHandler;
    private final CostService          costService;

    public CostSelectionPipelineStage(CostSelectionHandler selectionHandler, CostService costService) {
        this.selectionHandler = selectionHandler;
        this.costService = costService;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        boolean freeOpen = context.hasComponent(PipelineComponentKeys.FREE_OPEN);
        if (freeOpen) {
            chain.proceed(player, context);
            return;
        }

        boolean fastOpen = context.hasComponent(PipelineComponentKeys.FAST_OPEN);

        ProcessCallback<CostTypeOption> callback = new ProcessCallback<>() {

            @Override
            public void proceed(@Nullable CostTypeOption result) {
                if (result != null) {
                    setCostComponent(context, result, player, crate);
                }
                chain.proceed(player, context);
            }

            @Override
            public void cancel() {
                chain.abort();
            }
        };

        if (fastOpen) {
            this.selectionHandler.selectAnyAvailableCost(player, crate, callback);
        }
        else {
            this.selectionHandler.startSelection(player, crate, callback);
        }
    }

    private void setCostComponent(PipelineContext context, CostTypeOption option, Player player, Crate crate) {
        Identifier typeId = option.typeId();
        String optionId = option.optionId();

        CostPipelineComponent component = new DefaultCostPipelineComponent(typeId, optionId);
        context.putComponent(PipelineComponentKeys.COST, component);

        context.getComponent(PipelineComponentKeys.BATCH).ifPresent(batchComponent -> {
            int maxOpens = costService.getMaxAffordableOpens(player, crate, typeId, optionId);
            batchComponent.limitMaxAllowed(maxOpens);
        });
    }
}
