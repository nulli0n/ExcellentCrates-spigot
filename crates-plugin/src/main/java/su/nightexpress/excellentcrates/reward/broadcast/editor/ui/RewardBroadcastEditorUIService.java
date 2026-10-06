package su.nightexpress.excellentcrates.reward.broadcast.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.context.BroadcastSettingsMenuContext;

@NullMarked
public class RewardBroadcastEditorUIService {

    private final CoreUIService coreUI;

    public RewardBroadcastEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openSettingsMenu(Player player, BroadcastSettingsMenuContext context) {
        return this.coreUI.openMenu(player, RewardBroadcastEditorUIKeys.MENU_SETTINGS, context);
    }
}
