package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.CommandsBundleDeleteDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsBundleDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsGiveModeDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsIterationsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.context.RewardCommandsMenuContext;

@NullMarked
public class RewardCommandsEditorUIService {

    private final CoreUIService coreUI;

    public RewardCommandsEditorUIService(CoreUIService coreUIService) {
        this.coreUI = coreUIService;
    }

    public ActionResult openCommandsMenu(Player player, RewardCommandsMenuContext context) {
        return this.coreUI.openMenu(player, RewardCommandsEditorUIKeys.MENU_COMMANDS, context);
    }

    public ActionResult showCommandsIterationsDialog(Player player, RewardCommandsIterationsDialogContext context,
                                                     Runnable callback) {
        return this.coreUI.showDialog(player, RewardCommandsEditorUIKeys.DIALOG_ITERATIONS, context, callback);
    }

    public ActionResult showCommandsGiveModeDialog(Player player, RewardCommandsGiveModeDialogContext context,
                                                   Runnable callback) {
        return this.coreUI.showDialog(player, RewardCommandsEditorUIKeys.DIALOG_GIVE_MODE, context, callback);
    }

    public ActionResult showCommandsBundleDialog(Player player, RewardCommandsBundleDialogContext context,
                                                 Runnable callback) {
        return this.coreUI.showDialog(player, RewardCommandsEditorUIKeys.DIALOG_BUNDLE, context, callback);
    }

    public ActionResult showCommandsBundleDeleteDialog(Player player, CommandsBundleDeleteDialogContext context,
                                                       Runnable callback) {
        return this.coreUI.showDialog(player, RewardCommandsEditorUIKeys.DIALOG_BUNDLE_DELETE, context, callback);
    }
}
