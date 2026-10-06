package su.nightexpress.excellentcrates.crates.open;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.open.component.DefaultOpeningComponent;
import su.nightexpress.excellentcrates.crates.open.component.codec.OpeningComponentCodec;
import su.nightexpress.excellentcrates.crates.open.component.extension.CrateOpeningComponentDataExtension;
import su.nightexpress.excellentcrates.crates.open.editor.CrateOpeningEditorBootstrapContext;
import su.nightexpress.excellentcrates.crates.open.pipeline.CrateOpeningPipelineExecutor;
import su.nightexpress.excellentcrates.crates.open.pipeline.DefaultPipelineProcessor;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class CrateOpenActionsBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.open_actions");
    private static final String     NAME = "Open Actions";

    private final CrateOpeningComponentDataExtension dataExtension;
    private final CrateEditorExtension               editorExtension;
    private final CrateOpeningPipelineExecutor       pipelineExecutor;
    private final DefaultPipelineProcessor           pipelineProcessor;

    public CrateOpenActionsBootstrapContext(CratesPlugin plugin,
                                            CoreUIService coreUI,
                                            MessageDispatcher dispatcher,
                                            CrateResolver crateResolver,
                                            CratePlaceholders cratePlaceholders) {
        super(ID, NAME);

        ConfigCodecs.register(DefaultOpeningComponent.class, OpeningComponentCodec.INSTANCE);

        CrateOpeningEditorBootstrapContext editorContext = new CrateOpeningEditorBootstrapContext(
            plugin, coreUI, dispatcher, crateResolver, cratePlaceholders
        );

        this.dataExtension = new CrateOpeningComponentDataExtension();
        this.editorExtension = editorContext.extension;
        this.pipelineExecutor = new CrateOpeningPipelineExecutor(cratePlaceholders);
        this.pipelineProcessor = new DefaultPipelineProcessor();

        this.addComponent(editorContext);
    }

    public CrateOpeningComponentDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public CrateEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public CrateOpeningPipelineExecutor getPipelineExecutor() {
        return this.pipelineExecutor;
    }

    public DefaultPipelineProcessor getPipelineProcessor() {
        return this.pipelineProcessor;
    }
}
