package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.context.RewardCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu.context.RewardCooldownsMenuContext;

@NullMarked
public class RewardCooldownsEditorUIService {

    private final CoreUIService coreUI;

    public RewardCooldownsEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openCooldownsMenu(Player player, RewardCooldownsMenuContext context) {
        return this.coreUI.openMenu(player, RewardCooldownsEditorUIKeys.MENU_COOLDOWNS, context);
    }

    public ActionResult showCooldownSettingsDialog(Player player, RewardCooldownsSettingsDialogContext context,
                                                   Runnable callback) {
        return this.coreUI.showDialog(player, RewardCooldownsEditorUIKeys.DIALOG_OPTIONS, context, callback);
    }
}
