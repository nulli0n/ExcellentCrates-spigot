package su.nightexpress.excellentcrates.reward.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardPreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardManualCreationDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;

@NullMarked
public class RewardEditorUIService {

    private final CoreUIService coreUI;

    public RewardEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openCreationDialog(Player player, RewardManualCreationDialogContext context,
                                           Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_MANUAL_CREATION, context, callback);
    }

    public ActionResult openBrowseMenu(Player player, RewardBrowseMenuContext context) {
        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_BROWSE, context);
    }

    public ActionResult openOptionsMenu(Player player, RewardOptionsMenuContext context) {
        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_OPTIONS, context);
    }

    public ActionResult showRewardWeightDialog(Player player, RewardWeightDialogContext context,
                                               Runnable refreshUI) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_WEIGHT, context, refreshUI);
    }

    public ActionResult openPreviewMenu(Player player, RewardPreviewMenuContext context) {
        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_PREVIEW, context);
    }

    public ActionResult showDeletionDialog(Player player, RewardDeletionDialogContext dialogContext,
                                           Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_DELETION, dialogContext, callback);
    }

    public ActionResult showPreviewNameDialog(Player player, RewardPreviewDialogContext dialogContext,
                                              Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_PREVIEW_NAME, dialogContext, callback);
    }

    public ActionResult showPreviewLoreDialog(Player player, RewardPreviewDialogContext dialogContext,
                                              Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_PREVIEW_LORE, dialogContext, callback);
    }
}
