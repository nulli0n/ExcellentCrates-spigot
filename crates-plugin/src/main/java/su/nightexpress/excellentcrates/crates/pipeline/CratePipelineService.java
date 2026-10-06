package su.nightexpress.excellentcrates.crates.pipeline;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.event.CratePipelinePreStartEvent;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineProcessor;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.RegisteredPipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class CratePipelineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CratePipelineService.class);

    private final TinyRegistry<RegisteredPipelineStage> stages;
    private final TinyRegistry<PipelineExecutor>        executors;
    private final TinyRegistry<PipelineProcessor>       processors;

    public CratePipelineService(TinyRegistry<RegisteredPipelineStage> stages,
                                TinyRegistry<PipelineExecutor> executors,
                                TinyRegistry<PipelineProcessor> processors) {
        this.stages = stages;
        this.executors = executors;
        this.processors = processors;
    }

    private List<PipelineStage> compileStages() {
        return this.stages.getEntries().stream()
            .sorted(Comparator.comparingInt((RegisteredPipelineStage stage) -> stage.phase().ordinal())
                .thenComparingInt(stage -> stage.order()))
            .map(RegisteredPipelineStage::stage)
            .toList();
    }

    private @Nullable PipelineProcessor selectHighestPriorityProcessor(Crate crate, PipelineContext context) {
        return this.processors.getEntries().stream()
            .filter(processor -> processor.shouldHandle(crate, context))
            .max(Comparator.comparingInt(PipelineProcessor::getPriority))
            .orElse(null);
    }

    public void startPipeline(Player player, Crate crate) {
        this.startPipeline(player, crate, null);
    }

    public void startPipeline(Player player, Crate crate, @Nullable Consumer<PipelineContext> consumer) {
        PipelineContext context = new DefaultPipelineContext(crate);

        if (consumer != null) {
            consumer.accept(context);
        }

        List<PipelineStage> stages = this.compileStages();
        if (stages.isEmpty()) {
            LOGGER.error("Can not start pipeline for crate '{}': No pipeline stages available.", crate.id());
            return;
        }

        CratePipelinePreStartEvent preStartEvent = new CratePipelinePreStartEvent(player, crate, context, stages);
        Bukkit.getPluginManager().callEvent(preStartEvent);
        if (preStartEvent.isCancelled()) {
            return;
        }

        this.executeChain(player, crate, context, stages, 0);
    }

    private void executeChain(Player player,
                              Crate crate,
                              PipelineContext context,
                              List<PipelineStage> stages,
                              int index) {
        if (index >= stages.size()) {
            PipelineProcessor highestPriorityProcessor = this.selectHighestPriorityProcessor(crate, context);
            if (highestPriorityProcessor == null) {
                LOGGER.error("Can not finish pipeline for crate '{}': No pipeline processor available.",
                    crate.id()
                );
                return;
            }

            highestPriorityProcessor.process(player, context, () -> {
                this.executors.forEach(executor -> executor.execute(player, crate, context));
            });
            return;
        }

        PipelineStage currentStage = stages.get(index);

        currentStage.intercept(player, crate, context, new PipelineChain() {

            @Override
            public void proceed(Player player, PipelineContext context) {
                executeChain(player, crate, context, stages, index + 1);
            }

            @Override
            public void abort() {
                // Pipeline aborted, do nothing
            }
        });
    }
}
