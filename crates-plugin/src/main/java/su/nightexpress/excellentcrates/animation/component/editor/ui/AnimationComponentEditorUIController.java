package su.nightexpress.excellentcrates.animation.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.animation.component.editor.AnimationComponentEditorService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.context.AnimationComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.animation.component.editor.ui.menu.context.AnimationComponentMenuContext;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class AnimationComponentEditorUIController {

    private final CrateRegistry                     crateRegistry;
    private final AnimationComponentEditorService   editorService;
    private final AnimationComponentEditorUIService uiService;
    private final CrateMessageDispatcher            dispatcher;

    public AnimationComponentEditorUIController(CrateRegistry crateRegistry,
                                                AnimationComponentEditorService editorService,
                                                AnimationComponentEditorUIService uiService,
                                                CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        AnimationComponentMenuContext context = new AnimationComponentMenuContext(crateRef, hook, navigator);

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openComponentMenu(player, context));
    }

    public boolean onComponentMenuStateClick(Player player, Crate crate, CrateEditorHook hook, boolean newState) {
        return this.dispatcher.handleFeedbackBase(player, crate, this.editorService.setComponentState(hook, newState));
    }

    public void onComponentMenuAnimationClick(Player player, Crate crate, CrateEditorHook hook, AdaptedKey currentKey,
                                              Runnable refreshUI) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        AnimationComponentSelectionDialogContext context = new AnimationComponentSelectionDialogContext(
            crateRef, currentKey, hook
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showAnimationSelectionDialog(player, context, refreshUI)
        );
    }

    public boolean onSelectionDialogAnimationClick(Player player, Crate crate, CrateEditorHook hook,
                                                   AdaptedKey selectedKey) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setComponentAnimationKey(hook, selectedKey)
        );
    }
}
