package su.nightexpress.excellentcrates.crates.display.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIService;
import su.nightexpress.excellentcrates.crates.display.editor.ui.controller.DisplayEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.display.editor.ui.controller.DisplayEditorMenuLoader;
import su.nightexpress.excellentcrates.crates.display.editor.ui.extension.DisplayEditorExtension;

@NullMarked
public final class DisplayEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.display.editor");
    private static final String     NAME = "Editor";

    private final DisplayEditorExtension extension;

    public DisplayEditorContext(CratesPlugin plugin,
                                CoreUIService coreUI,
                                MessageDispatcher dispatcher,
                                CrateRegistry crates) {
        super(ID, NAME);

        DisplayEditorService editorService = new DisplayEditorService();
        DisplayEditorUIService uiService = new DisplayEditorUIService(coreUI);
        DisplayEditorUIController uiController = new DisplayEditorUIController(
            editorService, uiService, dispatcher
        );

        this.extension = new DisplayEditorExtension(uiController);

        this.addComponent(new DisplayEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new DisplayEditorMenuLoader(plugin, coreUI, crates, uiController));
    }

    public DisplayEditorExtension getEditorExtension() {
        return this.extension;
    }
}
