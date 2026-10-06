package su.nightexpress.excellentcrates.keys.common.base.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.common.base.editor.KeyBaseEditorService;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu.context.KeyBaseMainMenuContext;

@NullMarked
public class KeyBaseEditorUIController implements FeedbackHandler {

    private final KeyBaseEditorService   editorService;
    private final KeyBaseEditorUIService uiService;
    private final MessageDispatcher      dispatcher;

    public KeyBaseEditorUIController(KeyBaseEditorService editorService,
                                     KeyBaseEditorUIService uiService,
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
        KeyBaseMainMenuContext context = new KeyBaseMainMenuContext(keyId, hook, backNavigator);

        this.handleFeedback(player, this.uiService.openBaseMenu(player, context));
    }

    public boolean onBaseVirtualClick(Player player, KeyEditorHook hook, boolean virtual) {
        return this.handleFeedback(player, this.editorService.setVirtualState(hook, virtual));
    }
}
