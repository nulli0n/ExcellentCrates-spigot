package su.nightexpress.excellentcrates.crates.item.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.item.editor.ui.dialog.context.CrateItemIconDialogContext;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context.CrateItemSettingsMenuContext;

@NullMarked
public class CrateItemEditorUIService {

    private final CoreUIService coreUI;

    public CrateItemEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openSettingsMenu(Player player, CrateItemSettingsMenuContext context) {
        return this.coreUI.openMenu(player, CrateItemEditorUIKeys.MENU_SETTINGS, context);
    }

    public ActionResult showItemIconDialog(Player player, CrateItemIconDialogContext context, Runnable refreshUI) {
        return this.coreUI.showDialog(player, CrateItemEditorUIKeys.DIALOG_ICON, context, refreshUI);
    }
}
