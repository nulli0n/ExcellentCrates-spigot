package su.nightexpress.excellentcrates.animation.component.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIController;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.controller.AnimationComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.animation.component.editor.ui.controller.AnimationComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.animation.component.editor.ui.extension.AnimationComponentEditorExtension;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public class AnimationComponentEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("animation.component.editor");
    private static final String     NAME = "Component Editor";

    private final AnimationComponentEditorExtension editorExtension;

    public AnimationComponentEditorBootstrapContext(CratesPlugin plugin,
                                                    CoreUIService coreUI,
                                                    CrateMessageDispatcher dispatcher,
                                                    CrateRegistry crateRegistry,
                                                    AnimationRegistry openingRegistry) {
        super(ID, NAME);

        AnimationComponentEditorService editorService = new AnimationComponentEditorService();
        AnimationComponentEditorUIService uiService = new AnimationComponentEditorUIService(coreUI);
        AnimationComponentEditorUIController uiController = new AnimationComponentEditorUIController(
            crateRegistry, editorService, uiService, dispatcher
        );

        this.editorExtension = new AnimationComponentEditorExtension(uiController);

        this.addComponent(new AnimationComponentEditorDialogRegistrar(coreUI, openingRegistry, uiController));
        this.addComponent(new AnimationComponentEditorMenuRegistrar(plugin, coreUI, openingRegistry, uiController));
    }

    public AnimationComponentEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
