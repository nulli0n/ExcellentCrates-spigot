package su.nightexpress.excellentcrates.crates.cooldown.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIController;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIService;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.controller.CrateCooldownsEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.controller.CrateCooldownsEditorMenuRegistrar;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.extension.CrateCooldownsEditorExtension;

@NullMarked
public final class CrateCooldownsEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.cooldown.editor");
    private static final String     NAME = "Editor";

    public final CrateEditorExtension editorExtension;

    public CrateCooldownsEditorBootstrapContext(CratesPlugin plugin,
                                                CoreUIService coreUI,
                                                MessageDispatcher dispatcher,
                                                CrateResolver crateResolver,
                                                CratePlaceholders cratePlaceholders) {
        super(ID, NAME);

        CrateCooldownsEditorService editorService = new CrateCooldownsEditorService(cratePlaceholders);
        CrateCooldownsEditorUIService uiService = new CrateCooldownsEditorUIService(coreUI);
        CrateCooldownsEditorUIController uiController = new CrateCooldownsEditorUIController(
            editorService, uiService, dispatcher
        );

        this.editorExtension = new CrateCooldownsEditorExtension(uiController);

        this.addComponent(new CrateCooldownsEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new CrateCooldownsEditorMenuRegistrar(plugin, coreUI, crateResolver, uiController));
    }
}
