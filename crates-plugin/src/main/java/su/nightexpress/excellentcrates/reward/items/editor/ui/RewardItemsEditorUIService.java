package su.nightexpress.excellentcrates.reward.items.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.items.editor.ui.dialog.context.RewardItemAddDialogContext;
import su.nightexpress.excellentcrates.reward.items.editor.ui.menu.context.RewardItemsMenuContext;

@NullMarked
public class RewardItemsEditorUIService {

    private final CoreUIService coreUI;

    public RewardItemsEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openItemsMenu(Player player, RewardItemsMenuContext context) {
        return this.coreUI.openMenu(player, RewardItemsEditorUIKeys.MENU_ITEMS, context);
    }

    public ActionResult showAddItemDialog(Player player, RewardItemAddDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, RewardItemsEditorUIKeys.DIALOG_ADD_ITEM, context, callback);
    }
}
