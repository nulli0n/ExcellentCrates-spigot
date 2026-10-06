package su.nightexpress.excellentcrates.crates.open.editor.ui;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.open.editor.CrateOpeningEditorService;
import su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.context.CrateOpeningCommandsDialogContext;
import su.nightexpress.excellentcrates.crates.open.editor.ui.menu.context.CrateOpeningSettingsMenuContext;

@NullMarked
public class CrateOpeningEditorUIController implements FeedbackHandler {

    private final CrateOpeningEditorService   editorService;
    private final CrateOpeningEditorUIService uiService;
    private final MessageDispatcher           dispatcher;

    public CrateOpeningEditorUIController(CrateOpeningEditorService editorService,
                                          CrateOpeningEditorUIService uiService,
                                          MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player,
                                 Identifier crateId,
                                 CrateEditorHook hook,
                                 BackwardNavigator backwardNavigator) {

        CrateOpeningSettingsMenuContext menuContext = new CrateOpeningSettingsMenuContext(crateId, hook,
            backwardNavigator);

        this.handleFeedback(player, this.uiService.openOptionsMenu(player, menuContext));
    }

    public boolean onSettingsMenuStateClick(Player player, CrateEditorHook hook, boolean state) {
        return this.handleFeedback(player, this.editorService.setComponentState(hook, state));
    }

    public void onSettingsMenuCommandsClick(Player player, CrateEditorHook hook, List<String> commands,
                                            Runnable refreshUI) {
        CrateOpeningCommandsDialogContext dialogContext = new CrateOpeningCommandsDialogContext(commands, hook);
        this.handleFeedback(player, this.uiService.showCommandsDialog(player, dialogContext, refreshUI));
    }

    public boolean onCommandsDialogConfirm(Player player, CrateEditorHook hook, List<String> commands) {
        return this.handleFeedback(player, this.editorService.setComponentCommands(hook, commands));
    }
}
