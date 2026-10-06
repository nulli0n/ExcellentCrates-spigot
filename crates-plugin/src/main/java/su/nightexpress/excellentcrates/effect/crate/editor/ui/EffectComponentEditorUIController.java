package su.nightexpress.excellentcrates.effect.crate.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.effect.crate.editor.EffectComponentEditorService;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog.context.EffectComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.menu.context.EffectComponentEditorMainMenuContext;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class EffectComponentEditorUIController {

    private final CrateRegistry                  crateRegistry;
    private final EffectComponentEditorService   editorService;
    private final EffectComponentEditorUIService uiService;
    private final CrateMessageDispatcher         dispatcher;

    public EffectComponentEditorUIController(CrateRegistry crateRegistry,
                                             EffectComponentEditorService editorService,
                                             EffectComponentEditorUIService uiService,
                                             CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        EffectComponentEditorMainMenuContext context = new EffectComponentEditorMainMenuContext(
            crateRef, hook, navigator
        );

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openMainMenu(player, context));
    }

    public boolean onComponentMenuStateClick(Player player, Crate crate, CrateEditorHook hook, boolean newState) {
        return this.dispatcher.handleFeedbackBase(player, crate, this.editorService.setComponentState(hook, newState));
    }

    public void onComponentMenuProfileClick(Player player, Crate crate, CrateEditorHook hook, AdaptedKey currentKey,
                                            Runnable refreshUI) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        EffectComponentSelectionDialogContext context = new EffectComponentSelectionDialogContext(
            crateRef, currentKey, hook
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showSelectionDialog(player, context, refreshUI)
        );
    }

    public boolean onSelectionDialogProfileClick(Player player, Crate crate, CrateEditorHook hook,
                                                 AdaptedKey selectedKey) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setComponentProfileKey(hook, selectedKey)
        );
    }
}
