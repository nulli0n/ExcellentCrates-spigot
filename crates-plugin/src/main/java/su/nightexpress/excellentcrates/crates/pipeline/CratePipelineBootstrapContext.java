package su.nightexpress.excellentcrates.crates.pipeline;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineAPI;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineProcessor;
import su.nightexpress.excellentcrates.api.crate.pipeline.RegisteredPipelineStage;
import su.nightexpress.excellentcrates.crates.pipeline.command.CrateOpenCommand;
import su.nightexpress.excellentcrates.crates.pipeline.interact.CratePipelineInteractAction;

@NullMarked
public final class CratePipelineBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.pipeline");
    private static final String     BUNDLE_NAME = "Pipeline";

    public final TinyRegistry<RegisteredPipelineStage> stages;
    public final TinyRegistry<PipelineExecutor>        executors;
    public final TinyRegistry<PipelineProcessor>       processors;

    public final CratePipelineInteractAction interactAction;
    public final CrateOpenCommand            crateCommand;

    public final PipelineAPI api;

    public CratePipelineBootstrapContext(CrateMessageDispatcher dispatcher) {
        super(BUNDLE_ID, BUNDLE_NAME);

        this.stages = new SimpleRegistry<>();
        this.executors = new SimpleRegistry<>();
        this.processors = new SimpleRegistry<>();

        CratePipelineService pipeline = new CratePipelineService(stages, executors, processors);

        this.interactAction = new CratePipelineInteractAction(pipeline);
        this.crateCommand = new CrateOpenCommand(pipeline, dispatcher);

        this.api = new DefaultPipelineAPI(this.stages, this.executors, this.processors, pipeline);
    }
}
