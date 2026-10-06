package su.nightexpress.excellentcrates.animation.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.context.AnimationComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.animation.component.editor.ui.menu.context.AnimationComponentMenuContext;

@NullMarked
public class AnimationComponentEditorUIService {

    private final CoreUIService coreUI;

    public AnimationComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openComponentMenu(Player player, AnimationComponentMenuContext context) {
        return this.coreUI.openMenu(player, AnimationComponentEditorUIKeys.MENU_COMPONENT, context);
    }

    public ActionResult showAnimationSelectionDialog(Player player, AnimationComponentSelectionDialogContext context,
                                                     Runnable callback) {
        return this.coreUI.showDialog(player, AnimationComponentEditorUIKeys.DIALOG_SELECTION, context,
            callback);
    }
}
