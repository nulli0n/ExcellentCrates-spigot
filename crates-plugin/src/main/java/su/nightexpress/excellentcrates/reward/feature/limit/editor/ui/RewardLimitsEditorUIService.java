package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.context.RewardLimitOptionsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsAlternativeMenuContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsMainMenuContext;

@NullMarked
public class RewardLimitsEditorUIService {

    private final CoreUIService coreUI;

    public RewardLimitsEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openMainLimitsMenu(Player player, RewardLimitsMainMenuContext context) {
        return this.coreUI.openMenu(player, RewardLimitsEditorUIKeys.MENU_MAIN, context);
    }

    public ActionResult openAlternativeSelectionMenu(Player player, RewardLimitsAlternativeMenuContext context) {
        return this.coreUI.openMenu(player, RewardLimitsEditorUIKeys.MENU_ALTERNATIVE_SELECTION, context);
    }

    public ActionResult openLimitOptionsDialog(Player player, RewardLimitOptionsDialogContext context,
                                               Runnable callback) {
        return this.coreUI.showDialog(player, RewardLimitsEditorUIKeys.DIALOG_OPTIONS, context, callback);
    }
}
