package su.nightexpress.excellentcrates.crates.cooldown.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.cooldown.editor.CrateCooldownsEditorService;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.dialog.context.CrateCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.menu.context.CrateCooldownsMenuContext;

@NullMarked
public class CrateCooldownsEditorUIController implements FeedbackHandler {

    private final CrateCooldownsEditorService   editorService;
    private final CrateCooldownsEditorUIService uiService;
    private final MessageDispatcher             dispatcher;

    public CrateCooldownsEditorUIController(CrateCooldownsEditorService editorService,
                                            CrateCooldownsEditorUIService uiService,
                                            MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier crateId, CrateEditorHook hook,
                                 BackwardNavigator backwardNavigator) {
        CrateCooldownsMenuContext context = new CrateCooldownsMenuContext(crateId, hook, backwardNavigator);

        this.handleFeedback(player, this.uiService.openCooldownsMenu(player, context));
    }

    public void onCooldownsMenuCooldownClick(Player player, CooldownType type, CooldownSnapshot snapshot,
                                             CrateEditorHook hook,
                                             Runnable refreshUI) {

        CrateCooldownsSettingsDialogContext context = new CrateCooldownsSettingsDialogContext(type, snapshot, hook);

        this.handleFeedback(player, this.uiService.showCooldownSettingsDialog(player, context, refreshUI));
    }

    public boolean onCooldownsSettingsDialogSubmit(Player player, CooldownType type, CooldownSnapshot newSnapshot,
                                                   CrateEditorHook hook) {
        return this.handleFeedback(player, this.editorService.editCooldown(hook, type, newSnapshot));
    }
}
