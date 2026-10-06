package su.nightexpress.excellentcrates.preview.crate.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.preview.crate.editor.PreviewComponentEditorService;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.context.PreviewComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.context.PreviewComponentEditorMainMenuContext;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class PreviewComponentEditorUIController {

    private final CrateRegistry                   crateRegistry;
    private final PreviewComponentEditorService   editorService;
    private final PreviewComponentEditorUIService uiService;
    private final CrateMessageDispatcher          dispatcher;

    public PreviewComponentEditorUIController(CrateRegistry crateRegistry,
                                              PreviewComponentEditorService editorService,
                                              PreviewComponentEditorUIService uiService,
                                              CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        PreviewComponentEditorMainMenuContext context = new PreviewComponentEditorMainMenuContext(
            crateRef, hook, navigator
        );

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openMainMenu(player, context));
    }

    public boolean onComponentMenuStateClick(Player player, Crate crate, CrateEditorHook hook, boolean newState) {
        return this.dispatcher.handleFeedbackBase(player, crate, this.editorService.setComponentState(hook, newState));
    }

    public void onComponentMenuPreviewClick(Player player, Crate crate, CrateEditorHook hook, AdaptedKey currentKey,
                                            Runnable refreshUI) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        PreviewComponentSelectionDialogContext context = new PreviewComponentSelectionDialogContext(
            crateRef, currentKey, hook
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showSelectionDialog(player, context, refreshUI)
        );
    }

    public boolean onSelectionDialogPreviewClick(Player player, Crate crate, CrateEditorHook hook,
                                                 AdaptedKey selectedKey) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setComponentPreviewKey(hook, selectedKey)
        );
    }
}
