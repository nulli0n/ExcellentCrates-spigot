package su.nightexpress.excellentcrates.keys.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.data.KeyDataService;
import su.nightexpress.excellentcrates.keys.editor.command.EditorCommand;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIService;
import su.nightexpress.excellentcrates.keys.editor.ui.controller.KeyEditorDialogRegistrar;
import su.nightexpress.excellentcrates.keys.editor.ui.controller.KeyEditorMenuRegistrar;
import su.nightexpress.excellentcrates.keys.editor.validation.KeyIdService;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;

@NullMarked
public final class KeyEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.editor");
    private static final String     NAME = "Editor";

    public final TinyRegistry<KeyEditorExtension> extensions;

    public final KeyEditorService editorService;

    private final EditorCommand editorCommand;

    public KeyEditorBootstrapContext(CratesPlugin plugin,
                                     CoreUIService coreUI,
                                     CrateMessageDispatcher dispatcher,
                                     KeyRegistry keyRegistry,
                                     KeyItemFactory itemFactory,
                                     KeyDataService dataService) {
        super(ID, NAME);
        this.extensions = new SimpleRegistry<>();

        this.editorService = new KeyEditorService(dataService);

        KeyIdService idService = new KeyIdService(keyRegistry);
        KeyEditorUIService uiService = new KeyEditorUIService(coreUI);
        KeyEditorUIController uiController = new KeyEditorUIController(idService, this.editorService, uiService,
            dispatcher);

        this.editorCommand = new EditorCommand(uiService, dispatcher);

        this.addComponent(new KeyEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new KeyEditorMenuRegistrar(
            plugin, coreUI, keyRegistry, itemFactory, extensions, uiController)
        );
    }

    public EditorCommand getEditorCommand() {
        return this.editorCommand;
    }
}
