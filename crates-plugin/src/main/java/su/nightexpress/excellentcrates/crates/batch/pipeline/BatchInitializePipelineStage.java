package su.nightexpress.excellentcrates.crates.batch.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.crates.batch.pipeline.component.DefaultCrateBatchPipelineComponent;
import su.nightexpress.excellentcrates.crates.batch.settings.BatchSettings;

@NullMarked
public class BatchInitializePipelineStage implements PipelineStage {

    private final ReadOnlySettings<BatchSettings> settings;

    public BatchInitializePipelineStage(ReadOnlySettings<BatchSettings> settings) {
        this.settings = settings;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        int initialAmount = this.settings.get().maxBatchSize();
        BatchPipelineComponent batchComponent = new DefaultCrateBatchPipelineComponent(initialAmount);

        context.putComponent(PipelineComponentKeys.BATCH, batchComponent);
        chain.proceed(player, context);
    }
}
