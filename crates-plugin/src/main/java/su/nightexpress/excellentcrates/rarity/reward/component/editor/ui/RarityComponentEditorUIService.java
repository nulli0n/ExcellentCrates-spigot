package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.context.RarityComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu.context.RarityComponentMainMenuContext;

@NullMarked
public class RarityComponentEditorUIService {

    private final CoreUIService coreUI;

    public RarityComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openMainMenu(Player player, RarityComponentMainMenuContext context) {
        return this.coreUI.openMenu(player, RarityComponentEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult showSelectionDialog(Player player, RarityComponentSelectionDialogContext context,
                                            Runnable callback) {
        return this.coreUI.showDialog(player, RarityComponentEditorUIKeys.DIALOG_SELECTION, context, callback);
    }
}
