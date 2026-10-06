package su.nightexpress.excellentcrates.effect.crate.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog.context.EffectComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.menu.context.EffectComponentEditorMainMenuContext;

@NullMarked
public class EffectComponentEditorUIService {

    private final CoreUIService coreUI;

    public EffectComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openMainMenu(Player player, EffectComponentEditorMainMenuContext context) {
        return coreUI.openMenu(player, EffectComponentEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult showSelectionDialog(Player player, EffectComponentSelectionDialogContext context,
                                            Runnable callback) {
        return coreUI.showDialog(player, EffectComponentEditorUIKeys.DIALOG_SELECTION, context, callback);
    }
}
