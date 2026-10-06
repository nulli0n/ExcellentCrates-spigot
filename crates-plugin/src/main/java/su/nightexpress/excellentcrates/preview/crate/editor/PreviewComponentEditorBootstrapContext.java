package su.nightexpress.excellentcrates.preview.crate.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.crate.editor.lang.PreviewComponentLang;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIController;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIService;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.controller.PreviewComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.controller.PreviewComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.extension.PreviewComponentEditorExtension;

@NullMarked
public class PreviewComponentEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("preview.component.editor");
    private static final String     NAME = "Component Editor";

    private final PreviewComponentEditorExtension editorExtension;

    public PreviewComponentEditorBootstrapContext(CratesPlugin plugin,
                                                  CoreUIService coreUI,
                                                  CrateMessageDispatcher dispatcher,
                                                  CrateRegistry crateRegistry,
                                                  PreviewRegistry previews) {
        super(ID, NAME);

        plugin.injectLang(PreviewComponentLang.class);

        PreviewComponentEditorService editorService = new PreviewComponentEditorService();
        PreviewComponentEditorUIService uiService = new PreviewComponentEditorUIService(coreUI);
        PreviewComponentEditorUIController uiController = new PreviewComponentEditorUIController(
            crateRegistry, editorService, uiService, dispatcher
        );

        this.editorExtension = new PreviewComponentEditorExtension(uiController);

        this.addComponent(new PreviewComponentEditorDialogRegistrar(coreUI, previews, uiController));
        this.addComponent(new PreviewComponentEditorMenuRegistrar(plugin, coreUI, previews, uiController));
    }

    public PreviewComponentEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
