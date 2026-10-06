package su.nightexpress.excellentcrates.crates.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorAPI;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;
import su.nightexpress.excellentcrates.crates.editor.command.CrateEditorCommandExtension;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIService;
import su.nightexpress.excellentcrates.crates.editor.ui.controller.CrateEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.editor.ui.controller.CrateEditorMenuRegistrar;
import su.nightexpress.excellentcrates.crates.item.factory.CrateItemFactory;

@NullMarked
public class CrateEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.editor");
    private static final String     NAME = "Editor";

    public final CrateCommand                       editorCommand;
    public final TinyRegistry<CrateEditorExtension> extensions;

    public final CrateEditorAPI api;

    public CrateEditorContext(CratesPlugin plugin,
                              CoreUIService coreUI,
                              MessageDispatcher dispatcher,
                              CrateRegistry registry,
                              CrateItemFactory itemFactory,
                              CrateDataService dataService,
                              CratePlaceholders cratePlaceholders) {
        super(ID, NAME);
        this.extensions = new SimpleRegistry<>();

        CrateEditorService editorService = new CrateEditorService(dataService);
        CrateEditorUIService uiService = new CrateEditorUIService(coreUI);
        CrateEditorUIController uiController = new CrateEditorUIController(editorService, uiService, dispatcher);

        this.addComponent(
            new CrateEditorMenuRegistrar(
                plugin, coreUI, registry, itemFactory, cratePlaceholders, extensions, uiController
            )
        );
        this.addComponent(new CrateEditorDialogRegistrar(coreUI, uiController));

        this.editorCommand = new CrateEditorCommandExtension(uiService, dispatcher);
        this.api = new DefaultEditorAPI(this.extensions);
    }
}
