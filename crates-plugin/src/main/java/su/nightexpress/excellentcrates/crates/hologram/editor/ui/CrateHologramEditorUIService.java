package su.nightexpress.excellentcrates.crates.hologram.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramOffsetDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramTextDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.context.CrateHologramOptionsMenuContext;

@NullMarked
public class CrateHologramEditorUIService {

    private final CoreUIService coreUI;

    public CrateHologramEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openHologramOptionsMenu(Player player, CrateHologramOptionsMenuContext context) {
        return this.coreUI.openMenu(player, CrateHologramEditorUIKeys.MENU_OPTIONS, context);
    }

    public ActionResult showHologramTextDialog(Player player, CrateHologramTextDialogContext context,
                                               Runnable callback) {
        return this.coreUI.showDialog(player, CrateHologramEditorUIKeys.DIALOG_HOLOGRAM_TEXT, context, callback);
    }

    public ActionResult showHologramOffsetDialog(Player player, CrateHologramOffsetDialogContext context,
                                                 Runnable callback) {
        return this.coreUI.showDialog(player, CrateHologramEditorUIKeys.DIALOG_HOLOGRAM_OFFSET, context, callback);
    }
}
