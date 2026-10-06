package su.nightexpress.excellentcrates.keys.display.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayLoreDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayNameDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.menu.context.KeyDisplayMainMenuContext;

@NullMarked
public class KeyDisplayEditorUIService {

    private final CoreUIService coreUI;

    public KeyDisplayEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openMainMenu(Player player, KeyDisplayMainMenuContext context) {
        return this.coreUI.openMenu(player, KeyDisplayEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult showDisplayNameDialog(Player player, KeyDisplayNameDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, KeyDisplayEditorUIKeys.DIALOG_NAME, context, callback);
    }

    public ActionResult showDisplayLoreDialog(Player player, KeyDisplayLoreDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, KeyDisplayEditorUIKeys.DIALOG_LORE, context, callback);
    }
}
