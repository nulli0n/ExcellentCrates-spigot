package su.nightexpress.excellentcrates.keys.cost.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIService;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.controller.KeyCostEditorDialogRegistrar;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.controller.KeyCostEditorMenuRegistrar;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.extension.KeyCostEditorExtension;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;

@NullMarked
public final class KeyCostEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.cost.editor");
    private static final String     NAME = "Cost Editor";

    private final KeyCostEditorExtension editorExtension;

    public KeyCostEditorBootstrapContext(CratesPlugin plugin,
                                         CoreUIService coreUI,
                                         CrateMessageDispatcher dispatcher,
                                         CrateRegistry crateRegistry,
                                         KeyPlaceholders keyPlaceholders,
                                         KeyRegistry keyRegistry,
                                         KeyItemFactory keyItemFactory) {
        super(ID, NAME);

        KeyCostEditorService editorService = new KeyCostEditorService();
        KeyCostEditorUIService uiService = new KeyCostEditorUIService(coreUI);
        KeyCostEditorUIController uiController = new KeyCostEditorUIController(
            crateRegistry, editorService, uiService, dispatcher
        );

        this.addComponent(new KeyCostEditorDialogRegistrar(
            coreUI, keyRegistry, keyPlaceholders, uiController)
        );
        this.addComponent(new KeyCostEditorMenuRegistrar(
            plugin, coreUI, keyRegistry, keyItemFactory, uiController)
        );

        this.editorExtension = new KeyCostEditorExtension(uiController);
    }

    public KeyCostEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
