package su.nightexpress.excellentcrates.keys.editor.ui;

import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.editor.KeyEditorService;
import su.nightexpress.excellentcrates.keys.editor.context.KeyCreationContext;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyCreationDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyDeletionDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeyBrowseMenuContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeySettingsMenuContext;
import su.nightexpress.excellentcrates.keys.editor.validation.KeyIdService;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyEditorUIController implements FeedbackHandler {

    private final KeyIdService       idService;
    private final KeyEditorService   editorService;
    private final KeyEditorUIService uiService;
    private final MessageDispatcher  dispatcher;

    public KeyEditorUIController(KeyIdService idService,
                                 KeyEditorService editorService,
                                 KeyEditorUIService uiService,
                                 MessageDispatcher dispatcher) {
        this.idService = idService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return dispatcher;
    }

    public ActionResult onExtensionModify(Player player, Identifier rewardId, Function<CrateKey, ActionResult> action) {
        return this.editorService.modifyKey(rewardId, action);
    }

    public void onExtensionMoveBackward(Player player, KeySettingsMenuContext currentContext) {
        this.handleFeedback(player, this.uiService.openKeySettingsMenu(player, currentContext));
    }

    public void onManualCreationClick(Player player, Runnable refreshUI) {
        KeyCreationDialogContext dialogContext = new KeyCreationDialogContext(null, false);
        this.handleFeedback(player, this.uiService.openKeyCreationDialog(player, dialogContext, refreshUI));
    }

    public void onAutomaticCreationClick(Player player, ItemStack itemStack, Runnable refreshUI) {
        Identifier id = this.idService.createUniqueKeyId(itemStack).orElse(null);
        //String idName = id == null ? null : id.value(); // Back to string, so user can modify it in the dialog UI.
        if (id == null) {
            this.dispatcher.send(player, KeyEditorLang.CREATION_ID_GENERATION_FAILED, ctx -> ctx
                .with(SharedPlaceholders.ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            );
            return;
        }

        KeyCreationContext creationContext = new KeyCreationContext(id, itemStack);
        if (this.handleFeedback(player, this.editorService.createKey(creationContext))) {
            refreshUI.run();
        }
    }

    public void onListMenuKeyClick(Player player, Identifier keyId, KeyBrowseMenuContext currentContext) {
        BackwardNavigator navigator = user -> {
            this.handleFeedback(user, this.uiService.openKeyListMenu(user, currentContext));
        };

        KeySettingsMenuContext settingsContext = new KeySettingsMenuContext(keyId, navigator);

        this.handleFeedback(player, this.uiService.openKeySettingsMenu(player, settingsContext));
    }

    public void onOptionsMenuDeleteClick(Player player, KeySettingsMenuContext currentContext) {
        Identifier keyId = currentContext.keyId();
        KeyDeletionDialogContext dialogContext = new KeyDeletionDialogContext(keyId);

        this.handleFeedback(player, this.uiService.openKeyDeletionDialog(player, dialogContext, () -> {
            // Dialog callback will handle the menu navigation after deletion.
            currentContext.moveBackward(player);
        }));
    }

    public void onCreationDialogSubmit(Player player, String id) {
        Identifier keyId = IdentifierParser.parseSanitized(id).orElse(null);
        if (keyId == null) {
            this.dispatcher.send(player, KeyEditorLang.CREATION_INVALID_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> id)
            );
            return;
        }

        KeyCreationContext creationContext = new KeyCreationContext(keyId, null);
        this.handleFeedback(player, this.editorService.createKey(creationContext));
    }

    public boolean onDialogDeletionConfirmClick(Player player, Identifier keyId) {
        // Dialog will handle the callback to move backward in the menu based on the result of this action.
        return this.handleFeedback(player, this.editorService.deleteKey(keyId));
    }
}
