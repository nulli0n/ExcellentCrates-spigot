package su.nightexpress.excellentcrates.keys.item.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIController;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIService;
import su.nightexpress.excellentcrates.keys.item.editor.ui.controller.KeyItemEditorDialogRegistrar;
import su.nightexpress.excellentcrates.keys.item.editor.ui.controller.KeyItemEditorMenuRegistrar;
import su.nightexpress.excellentcrates.keys.item.editor.ui.extension.KeyItemEditorExtension;

@NullMarked
public final class KeyItemEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.item.editor");
    private static final String     NAME = "Editor";

    public final KeyEditorExtension extension;

    public KeyItemEditorBootstrapContext(CratesPlugin plugin,
                                         CoreUIService coreUI,
                                         MessageDispatcher dispatcher,
                                         KeyRegistry keyRegistry,
                                         KeyItemFactory itemFactory) {
        super(ID, NAME);

        KeyItemEditorService editorService = new KeyItemEditorService();
        KeyItemEditorUIService uiService = new KeyItemEditorUIService(coreUI);
        KeyItemEditorUIController controller = new KeyItemEditorUIController(editorService, uiService, dispatcher);

        this.extension = new KeyItemEditorExtension(itemFactory, controller);

        this.addComponent(new KeyItemEditorDialogRegistrar(coreUI, controller));
        this.addComponent(new KeyItemEditorMenuRegistrar(plugin, coreUI, keyRegistry, itemFactory, controller));
    }
}
