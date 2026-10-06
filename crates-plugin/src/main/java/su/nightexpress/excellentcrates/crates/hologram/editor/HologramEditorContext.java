package su.nightexpress.excellentcrates.crates.hologram.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIService;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.controller.CrateHologramEditorDialogRegistrar;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.controller.CrateHologramEditorMenuRegistrar;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.extension.CrateHologramEditorExtension;

@NullMarked
public class HologramEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.holograms.editor");
    private static final String     NAME = "Editor";

    private final CrateHologramEditorExtension extension;

    public HologramEditorContext(CratesPlugin plugin,
                                 CoreUIService coreUI,
                                 MessageDispatcher dispatcher,
                                 CrateRegistry crateRegistry,
                                 HologramDisplayService displayService,
                                 CratePlaceholders cratePlaceholders) {
        super(ID, NAME);

        HologramEditorService editorService = new HologramEditorService(displayService, cratePlaceholders);

        CrateHologramEditorUIService uiService = new CrateHologramEditorUIService(coreUI);
        CrateHologramEditorUIController uiController = new CrateHologramEditorUIController(editorService, uiService,
            dispatcher);

        this.extension = new CrateHologramEditorExtension(uiController);

        this.addComponent(new CrateHologramEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new CrateHologramEditorMenuRegistrar(plugin, coreUI, crateRegistry, uiController));
    }

    public CrateHologramEditorExtension getEditorExtension() {
        return this.extension;
    }
}
