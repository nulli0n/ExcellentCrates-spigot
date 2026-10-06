package su.nightexpress.excellentcrates.crates.batch.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.batch.BatchSelectionHandler;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.crates.batch.settings.BatchSettings;

@NullMarked
public class BatchSelectionPipelineStage implements PipelineStage {

    private final BatchSelectionHandler           selectionHandler;
    private final ReadOnlySettings<BatchSettings> settings;

    public BatchSelectionPipelineStage(BatchSelectionHandler selectionHandler,
                                       ReadOnlySettings<BatchSettings> settings) {
        this.selectionHandler = selectionHandler;
        this.settings = settings;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        BatchPipelineComponent batchComponent = context.getComponentOrNull(PipelineComponentKeys.BATCH);
        if (batchComponent == null) {
            chain.proceed(player, context);
            return;
        }

        boolean fastOpen = context.hasComponent(PipelineComponentKeys.FAST_OPEN);
        int maxAllowed = batchComponent.getMaxAllowed();

        if (fastOpen) {
            batchComponent.setSelectedAmount(maxAllowed);
            chain.proceed(player, context);
            return;
        }

        this.selectionHandler.startSelection(player, crate, maxAllowed, new ProcessCallback<Integer>() {

            @Override
            public void proceed(@Nullable Integer result) {
                if (result != null) {
                    int maxBatchSize = settings.get().maxBatchSize();
                    batchComponent.setSelectedAmount(Math.min(result, maxBatchSize));
                }
                chain.proceed(player, context);
            }

            @Override
            public void cancel() {
                chain.abort();
            }
        });
    }
}
