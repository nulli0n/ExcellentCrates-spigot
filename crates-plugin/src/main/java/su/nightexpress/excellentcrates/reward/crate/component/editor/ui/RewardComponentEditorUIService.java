package su.nightexpress.excellentcrates.reward.crate.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.context.RewardRollCountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.context.RewardComponentSettingsMenuContext;

@NullMarked
public class RewardComponentEditorUIService {

    private final CoreUIService coreUI;

    public RewardComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openRewardsMenu(Player player, RewardComponentSettingsMenuContext menuContext) {
        return this.coreUI.openMenu(player, RewardComponentEditorUIKeys.MENU_SETTINGS, menuContext);
    }

    public ActionResult showRewardsAmountDialog(Player player, RewardRollCountDialogContext context,
                                                Runnable refreshUI) {
        return this.coreUI.showDialog(player, RewardComponentEditorUIKeys.DIALOG_ROLL_COUNT, context,
            refreshUI);
    }
}
