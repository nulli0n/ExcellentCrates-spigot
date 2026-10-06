package su.nightexpress.excellentcrates.cost.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostCategoriesMenuContext;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostOptionsMenuContext;

@NullMarked
public class CostUIService {

    private final CoreUIService coreUI;

    public CostUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openCategoriesMenu(Player player, CostCategoriesMenuContext context) {
        return this.coreUI.openMenu(player, CostUIKeys.CATEGORIES, context);
    }

    public ActionResult openOptionsMenu(Player player, CostOptionsMenuContext context) {
        return this.coreUI.openMenu(player, CostUIKeys.OPTIONS, context);
    }
}
