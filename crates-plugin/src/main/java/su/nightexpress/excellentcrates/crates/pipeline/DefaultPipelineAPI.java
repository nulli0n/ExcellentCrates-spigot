package su.nightexpress.excellentcrates.crates.pipeline;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineAPI;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelinePhase;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineProcessor;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.RegisteredPipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class DefaultPipelineAPI implements PipelineAPI {

    private final TinyRegistry<RegisteredPipelineStage> interceptors;
    private final TinyRegistry<PipelineExecutor>        executors;
    private final TinyRegistry<PipelineProcessor>       processors;

    private final CratePipelineService pipeline;

    public DefaultPipelineAPI(TinyRegistry<RegisteredPipelineStage> interceptors,
                              TinyRegistry<PipelineExecutor> executors,
                              TinyRegistry<PipelineProcessor> processors,
                              CratePipelineService pipeline) {
        this.interceptors = interceptors;
        this.executors = executors;
        this.processors = processors;
        this.pipeline = pipeline;
    }

    @Override
    public void registerStage(PipelinePhase phase, int order, PipelineStage stage) {
        this.interceptors.register(new RegisteredPipelineStage(phase, order, stage));
    }

    @Override
    public void registerExecutor(PipelineExecutor executor) {
        this.executors.register(executor);
    }

    @Override
    public void registerProcessor(PipelineProcessor processor) {
        this.processors.register(processor);
    }

    @Override
    public void startPipeline(Player player, Crate crate) {
        this.pipeline.startPipeline(player, crate);
    }

    @Override
    public void startPipeline(Player player, Crate crate, @Nullable Consumer<PipelineContext> consumer) {
        this.pipeline.startPipeline(player, crate, consumer);
    }
}
