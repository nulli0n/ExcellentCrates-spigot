package su.nightexpress.excellentcrates.effect.crate.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIService;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.controller.EffectComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.controller.EffectComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.extension.EffectComponentEditorExtension;

@NullMarked
public class EffectComponentEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("effects.crate.component.editor");
    private static final String     NAME = "Editor";

    private final EffectComponentEditorExtension editorExtension;

    public EffectComponentEditorBootstrapContext(CratesPlugin plugin,
                                                 CoreUIService coreUI,
                                                 CrateMessageDispatcher dispatcher,
                                                 CrateRegistry crateRegistry,
                                                 EffectRegistry effectRegistry) {
        super(ID, NAME);

        EffectComponentEditorService editorService = new EffectComponentEditorService();
        EffectComponentEditorUIService uiService = new EffectComponentEditorUIService(coreUI);
        EffectComponentEditorUIController uiController = new EffectComponentEditorUIController(
            crateRegistry, editorService, uiService, dispatcher
        );

        this.editorExtension = new EffectComponentEditorExtension(uiController);

        this.addComponent(new EffectComponentEditorDialogRegistrar(coreUI, effectRegistry, uiController));
        this.addComponent(new EffectComponentEditorMenuRegistrar(plugin, coreUI, effectRegistry, uiController));
    }

    public EffectComponentEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
