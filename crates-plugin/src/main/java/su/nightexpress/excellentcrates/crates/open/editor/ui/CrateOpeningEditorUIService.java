package su.nightexpress.excellentcrates.crates.open.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.context.CrateOpeningCommandsDialogContext;
import su.nightexpress.excellentcrates.crates.open.editor.ui.menu.context.CrateOpeningSettingsMenuContext;

@NullMarked
public class CrateOpeningEditorUIService {

    private final CoreUIService coreUI;

    public CrateOpeningEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openOptionsMenu(Player player, CrateOpeningSettingsMenuContext context) {
        return this.coreUI.openMenu(player, CrateOpeningEditorUIKeys.MENU_SETTINGS, context);
    }

    public ActionResult showCommandsDialog(Player player, CrateOpeningCommandsDialogContext context,
                                           Runnable callback) {
        return this.coreUI.showDialog(player, CrateOpeningEditorUIKeys.DIALOG_COMMANDS, context, callback);
    }
}
