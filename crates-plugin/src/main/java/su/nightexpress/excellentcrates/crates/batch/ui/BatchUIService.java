package su.nightexpress.excellentcrates.crates.batch.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.batch.ui.menu.context.BatchAmountSelectionMenuContext;

@NullMarked
public class BatchUIService {

    private final CoreUIService coreUI;

    public BatchUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openAmountSelectionMenu(Player player, BatchAmountSelectionMenuContext context) {
        return this.coreUI.openMenu(player, BatchUIKeys.AMOUNT_SELECTION, context);
    }
}
