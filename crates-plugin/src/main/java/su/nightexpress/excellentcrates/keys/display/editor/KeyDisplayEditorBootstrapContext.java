package su.nightexpress.excellentcrates.keys.display.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIService;
import su.nightexpress.excellentcrates.keys.display.editor.ui.controller.KeyDisplayEditorDialogRegistrar;
import su.nightexpress.excellentcrates.keys.display.editor.ui.controller.KeyDisplayEditorMenuRegistrar;
import su.nightexpress.excellentcrates.keys.display.editor.ui.extension.KeyDisplayEditorExtension;

@NullMarked
public final class KeyDisplayEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.display.editor");
    private static final String     NAME = "Key Display Editor";

    public final KeyEditorExtension extension;

    public KeyDisplayEditorBootstrapContext(CratesPlugin plugin,
                                            CoreUIService coreUI,
                                            MessageDispatcher dispatcher,
                                            KeyRegistry keyRegistry) {
        super(ID, NAME);

        KeyDisplayEditorService editorService = new KeyDisplayEditorService();
        KeyDisplayEditorUIService uiService = new KeyDisplayEditorUIService(coreUI);
        KeyDisplayEditorUIController uiController = new KeyDisplayEditorUIController(editorService, uiService,
            dispatcher);

        this.extension = new KeyDisplayEditorExtension(uiController);

        this.addComponent(new KeyDisplayEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new KeyDisplayEditorMenuRegistrar(plugin, coreUI, keyRegistry, uiController));
    }
}
