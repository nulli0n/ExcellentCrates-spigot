package su.nightexpress.excellentcrates.crates.item.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIController;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIService;
import su.nightexpress.excellentcrates.crates.item.editor.ui.controller.CrateItemEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.item.editor.ui.controller.CrateItemEditorMenuRegistrar;
import su.nightexpress.excellentcrates.crates.item.editor.ui.extension.CrateItemEditorExtension;

@NullMarked
public final class CrateItemEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.item.editor");
    private static final String     NAME = "Crate Item Editor";

    public final CrateItemEditorExtension extension;

    public CrateItemEditorBootstrapContext(CratesPlugin plugin,
                                           CoreUIService coreUI,
                                           MessageDispatcher dispatcher,
                                           CrateRegistry crateRegistry,
                                           ICrateItemFactory itemFactory) {
        super(ID, NAME);

        CrateItemEditorService editorService = new CrateItemEditorService();
        CrateItemEditorUIService uiService = new CrateItemEditorUIService(coreUI);
        CrateItemEditorUIController uiController = new CrateItemEditorUIController(
            editorService, uiService, dispatcher
        );

        this.extension = new CrateItemEditorExtension(itemFactory, uiController);

        this.addComponent(new CrateItemEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new CrateItemEditorMenuRegistrar(plugin, coreUI, crateRegistry, itemFactory, uiController));
    }
}
