package su.nightexpress.excellentcrates.keys.item.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.item.editor.KeyItemEditorUIKeys;
import su.nightexpress.excellentcrates.keys.item.editor.ui.context.KeyItemDialogContext;
import su.nightexpress.excellentcrates.keys.item.editor.ui.menu.context.KeyItemMainMenuContext;

@NullMarked
public class KeyItemEditorUIService {

    private final CoreUIService coreUI;

    public KeyItemEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openItemMenu(Player player, KeyItemMainMenuContext context) {
        return this.coreUI.openMenu(player, KeyItemEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult showItemIconDialog(Player player, KeyItemDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, KeyItemEditorDialogKeys.ITEM, context, callback);
    }
}
