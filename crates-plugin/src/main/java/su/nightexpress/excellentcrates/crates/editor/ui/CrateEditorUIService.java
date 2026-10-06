package su.nightexpress.excellentcrates.crates.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateCreationDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateDeletionDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateBrowseMenuContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateOptionsMenuContext;

@NullMarked
public class CrateEditorUIService {

    private final CoreUIService coreUI;

    public CrateEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openCrateBrowseMenu(Player player, CrateBrowseMenuContext context) {
        return this.coreUI.openMenu(player, CrateEditorUIKeys.MENU_BROWSE, context);
    }

    public ActionResult openCrateOptionsMenu(Player player, CrateOptionsMenuContext context) {
        return this.coreUI.openMenu(player, CrateEditorUIKeys.MENU_OPTIONS, context);
    }

    public ActionResult showCrateCreationDialog(Player player, CrateCreationDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, CrateEditorUIKeys.DIALOG_CREATION, context, callback);
    }

    public ActionResult showCrateDeletionDialog(Player player, CrateDeletionDialogContext context) {
        return this.coreUI.showDialog(player, CrateEditorUIKeys.DIALOG_DELETION, context, null);
    }
}
