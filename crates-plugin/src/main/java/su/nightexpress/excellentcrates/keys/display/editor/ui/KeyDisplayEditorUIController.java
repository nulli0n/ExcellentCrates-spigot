package su.nightexpress.excellentcrates.keys.display.editor.ui;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.display.editor.KeyDisplayEditorService;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayLoreDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayNameDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.menu.context.KeyDisplayMainMenuContext;

@NullMarked
public class KeyDisplayEditorUIController implements FeedbackHandler {

    private final KeyDisplayEditorService   editorService;
    private final KeyDisplayEditorUIService uiService;
    private final MessageDispatcher         dispatcher;

    public KeyDisplayEditorUIController(KeyDisplayEditorService editorService,
                                        KeyDisplayEditorUIService uiService,
                                        MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier keyId, KeyEditorHook hook, BackwardNavigator backNavigator) {
        KeyDisplayMainMenuContext context = new KeyDisplayMainMenuContext(keyId, hook, backNavigator);

        this.handleFeedback(player, this.uiService.openMainMenu(player, context));
    }

    public void onDisplayMenuNameClick(Player player, KeyEditorHook hook, String currentName,
                                       Runnable refreshUI) {
        KeyDisplayNameDialogContext dialogContext = new KeyDisplayNameDialogContext(hook, currentName);

        this.handleFeedback(player, this.uiService.showDisplayNameDialog(player, dialogContext, refreshUI));
    }

    public void onDisplayMenuLoreClick(Player player, KeyEditorHook hook, List<String> currentLore,
                                       Runnable refreshUI) {
        KeyDisplayLoreDialogContext dialogContext = new KeyDisplayLoreDialogContext(hook, currentLore);

        this.handleFeedback(player, this.uiService.showDisplayLoreDialog(player, dialogContext, refreshUI));
    }

    public void onDisplayNameDialogSubmit(Player player, KeyDisplayNameDialogContext context, String newName) {
        KeyEditorHook hook = context.hook();
        this.handleFeedback(player, this.editorService.setDisplayName(hook, newName));
    }

    public void onDisplayLoreDialogSubmit(Player player, KeyDisplayLoreDialogContext context, List<String> newLore) {
        KeyEditorHook hook = context.hook();
        this.handleFeedback(player, this.editorService.setDisplayLore(hook, newLore));
    }
}
