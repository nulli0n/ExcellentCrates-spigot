package su.nightexpress.excellentcrates.crates.display.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context.CrateDisplayDialogContext;
import su.nightexpress.excellentcrates.crates.display.editor.ui.menu.context.CrateDisplaySettingsMenuContext;

@NullMarked
public class DisplayEditorUIService {

    private final CoreUIService coreuI;

    public DisplayEditorUIService(CoreUIService coreuI) {
        this.coreuI = coreuI;
    }

    public ActionResult openSettingsMenu(Player player, CrateDisplaySettingsMenuContext context) {
        return this.coreuI.openMenu(player, CrateDisplayEditorUIKeys.MENU_SETTINGS, context);
    }

    public ActionResult showDisplayNameDialog(Player player, CrateDisplayDialogContext context, Runnable refreshUI) {
        return this.coreuI.showDialog(player, CrateDisplayEditorUIKeys.DIALOG_NAME, context, refreshUI);
    }

    public ActionResult showDisplayLoreDialog(Player player, CrateDisplayDialogContext context, Runnable refreshUI) {
        return this.coreuI.showDialog(player, CrateDisplayEditorUIKeys.DIALOG_LORE, context, refreshUI);
    }
}
