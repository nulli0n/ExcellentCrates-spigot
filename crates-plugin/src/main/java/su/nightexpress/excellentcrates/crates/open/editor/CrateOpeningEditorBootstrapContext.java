package su.nightexpress.excellentcrates.crates.open.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIController;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIService;
import su.nightexpress.excellentcrates.crates.open.editor.ui.controller.CrateOpeningEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.open.editor.ui.controller.CrateOpeningEditorMenuRegistrar;
import su.nightexpress.excellentcrates.crates.open.editor.ui.extension.CrateOpeningEditorExtension;

@NullMarked
public final class CrateOpeningEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.opening.editor");
    private static final String     NAME = "Editor";

    public final CrateOpeningEditorExtension extension;

    public CrateOpeningEditorBootstrapContext(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              MessageDispatcher dispatcher,
                                              CrateResolver crateResolver,
                                              CratePlaceholders cratePlaceholders) {
        super(ID, NAME);

        CrateOpeningEditorService editorService = new CrateOpeningEditorService(cratePlaceholders);
        CrateOpeningEditorUIService uiService = new CrateOpeningEditorUIService(coreUI);
        CrateOpeningEditorUIController uiController = new CrateOpeningEditorUIController(
            editorService, uiService, dispatcher
        );

        this.extension = new CrateOpeningEditorExtension(uiController);

        this.addComponent(new CrateOpeningEditorMenuRegistrar(plugin, coreUI, crateResolver, uiController));
        this.addComponent(new CrateOpeningEditorDialogRegistrar(coreUI, uiController));
    }
}
