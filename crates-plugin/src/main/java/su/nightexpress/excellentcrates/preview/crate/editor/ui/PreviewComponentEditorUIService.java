package su.nightexpress.excellentcrates.preview.crate.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.context.PreviewComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.context.PreviewComponentEditorMainMenuContext;

@NullMarked
public class PreviewComponentEditorUIService {

    private final CoreUIService coreUI;

    public PreviewComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openMainMenu(Player player, PreviewComponentEditorMainMenuContext context) {
        return coreUI.openMenu(player, PreviewComponentEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult showSelectionDialog(Player player, PreviewComponentSelectionDialogContext context,
                                            Runnable callback) {
        return coreUI.showDialog(player, PreviewComponentEditorUIKeys.DIALOG_SELECTION, context, callback);
    }
}
