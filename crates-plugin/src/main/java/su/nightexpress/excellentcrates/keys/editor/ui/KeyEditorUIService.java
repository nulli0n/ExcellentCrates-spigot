package su.nightexpress.excellentcrates.keys.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyCreationDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyDeletionDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeyBrowseMenuContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeySettingsMenuContext;

@NullMarked
public class KeyEditorUIService {

    private final CoreUIService coreUI;

    public KeyEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openKeyListMenu(Player player, KeyBrowseMenuContext context) {
        return this.coreUI.openMenu(player, KeyEditorUIKeys.MENU_BROWSE, context);
    }

    public ActionResult openKeySettingsMenu(Player player, KeySettingsMenuContext context) {
        return this.coreUI.openMenu(player, KeyEditorUIKeys.MENU_SETTINGS, context);
    }

    public ActionResult openKeyCreationDialog(Player player, KeyCreationDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, KeyEditorUIKeys.DIALOG_CREATION, context, callback);
    }

    public ActionResult openKeyDeletionDialog(Player player, KeyDeletionDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, KeyEditorUIKeys.DIALOG_DELETION, context, callback);
    }
}
