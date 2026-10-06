package su.nightexpress.excellentcrates.reward.crate.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardsRequiredAmountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntrySelectMenuContext;

@NullMarked
public class RewardComponentEditorUIService {

    private final CoreUIService coreUI;

    public RewardComponentEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openRewardsMenu(Player player, RewardEntryBrowseMenuContext menuContext) {
        return this.coreUI.openMenu(player, RewardComponentEditorUIKeys.MENU_BROWSE, menuContext);
    }

    public ActionResult openRewardOptionsMenu(Player player, RewardEntryOptionsMenuContext menuContext) {
        return this.coreUI.openMenu(player, RewardComponentEditorUIKeys.MENU_OPTIONS, menuContext);
    }

    public ActionResult openRewardSelectionMenu(Player player, RewardEntrySelectMenuContext menuContext) {
        return this.coreUI.openMenu(player, RewardComponentEditorUIKeys.MENU_SELECT, menuContext);
    }

    public ActionResult showRewardWeightDialog(Player player, RewardWeightDialogContext context,
                                               Runnable refreshUI) {
        return this.coreUI.showDialog(player, RewardComponentEditorUIKeys.DIALOG_WEIGHT, context, refreshUI);
    }

    public ActionResult showRewardsAmountDialog(Player player, RewardsRequiredAmountDialogContext context,
                                                Runnable refreshUI) {
        return this.coreUI.showDialog(player, RewardComponentEditorUIKeys.DIALOG_REQUIRED_AMOUNT, context,
            refreshUI);
    }
}
