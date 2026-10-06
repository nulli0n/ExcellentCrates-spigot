package su.nightexpress.excellentcrates.crates.display.editor.ui;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.display.editor.DisplayEditorService;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context.CrateDisplayDialogContext;
import su.nightexpress.excellentcrates.crates.display.editor.ui.menu.context.CrateDisplaySettingsMenuContext;

@NullMarked
public class DisplayEditorUIController implements FeedbackHandler {

    private final DisplayEditorService   editorService;
    private final DisplayEditorUIService uiService;
    private final MessageDispatcher      dispatcher;

    public DisplayEditorUIController(DisplayEditorService editorService,
                                     DisplayEditorUIService uiService,
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
        CrateDisplaySettingsMenuContext menuContext = new CrateDisplaySettingsMenuContext(crateId, hook,
            backwardNavigator);

        this.handleFeedback(player, this.uiService.openSettingsMenu(player, menuContext));
    }

    public void onSettingsMenuNameClick(Player player, CrateDisplaySettingsMenuContext currentContext,
                                        ICrateDisplay display,
                                        Runnable refreshUI) {
        CrateDisplayDialogContext dialogContext = new CrateDisplayDialogContext(currentContext.crateId(), display,
            currentContext
                .hook());

        this.handleFeedback(player, this.uiService.showDisplayNameDialog(player, dialogContext, refreshUI));
    }

    public void onSettingsMenuLoreClick(Player player, CrateDisplaySettingsMenuContext currentContext,
                                        ICrateDisplay display,
                                        Runnable refreshUI) {
        CrateDisplayDialogContext dialogContext = new CrateDisplayDialogContext(currentContext.crateId(), display,
            currentContext
                .hook());

        this.handleFeedback(player, this.uiService.showDisplayLoreDialog(player, dialogContext, refreshUI));
    }

    public void onSettingsNameDialogApply(Player player, CrateEditorHook hook, String name) {
        this.editorService.setDisplayName(hook, name);
    }

    public void onSettingsLoreDialogApply(Player player, CrateEditorHook hook, List<String> lore) {
        this.editorService.setDisplayLore(hook, lore);
    }
}
