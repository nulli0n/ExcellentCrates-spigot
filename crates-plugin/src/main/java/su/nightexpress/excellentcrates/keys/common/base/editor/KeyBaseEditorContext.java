package su.nightexpress.excellentcrates.keys.common.base.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIController;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIService;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.controller.KeyBaseEditorMenuRegistrar;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.extension.KeyBaseEditorExtension;

@NullMarked
public final class KeyBaseEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.base.editor");
    private static final String     NAME = "Editor";

    public final KeyEditorExtension editorExtension;

    public KeyBaseEditorContext(CratesPlugin plugin,
                                MessageDispatcher dispatcher,
                                CoreUIService coreUI,
                                KeyRegistry keyRegistry) {
        super(ID, NAME);

        KeyBaseEditorService editorService = new KeyBaseEditorService();
        KeyBaseEditorUIService uiService = new KeyBaseEditorUIService(coreUI);
        KeyBaseEditorUIController uiController = new KeyBaseEditorUIController(editorService, uiService, dispatcher);

        this.addComponent(new KeyBaseEditorMenuRegistrar(plugin, coreUI, keyRegistry, uiController));

        this.editorExtension = new KeyBaseEditorExtension(uiController);
    }
}
