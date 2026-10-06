package su.nightexpress.excellentcrates.reward.selectable.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.selectable.ui.menu.context.RewardSelectionMenuContext;

@NullMarked
public class SelectiveUIService {

    private final CoreUIService coreUI;

    public SelectiveUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openSelectionMenu(Player player, RewardSelectionMenuContext context) {
        return this.coreUI.openMenu(player, SelectiveUIKeys.MENU_SELECTION, context);
    }
}
