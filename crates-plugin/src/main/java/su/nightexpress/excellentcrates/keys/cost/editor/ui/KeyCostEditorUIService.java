package su.nightexpress.excellentcrates.keys.cost.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostAddEntryDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryAmountDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryRemoveDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntriesMenuContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntryMenuContext;

@NullMarked
public class KeyCostEditorUIService {

    private final CoreUIService coreUI;

    public KeyCostEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openEntriesMenu(Player player, KeyCostEntriesMenuContext context) {
        return this.coreUI.openMenu(player, KeyCostEditorUIKeys.MENU_ENTRIES, context);
    }

    public ActionResult openEntryMenu(Player player, KeyCostEntryMenuContext context) {
        return this.coreUI.openMenu(player, KeyCostEditorUIKeys.MENU_ENTRY, context);
    }

    public ActionResult showAddEntryDialog(Player player, KeyCostAddEntryDialogContext context,
                                           Runnable callback) {
        return this.coreUI.showDialog(player, KeyCostEditorUIKeys.DIALOG_ADD_ENTRY, context, callback);
    }

    public ActionResult showEntryAmountDialog(Player player, KeyCostEntryAmountDialogContext context,
                                              Runnable callback) {
        return this.coreUI.showDialog(player, KeyCostEditorUIKeys.DIALOG_ENTRY_AMOUNT, context, callback);
    }

    public ActionResult showRemoveEntryDialog(Player player, KeyCostEntryRemoveDialogContext context,
                                              Runnable callback) {
        return this.coreUI.showDialog(player, KeyCostEditorUIKeys.DIALOG_REMOVE_ENTRY, context, callback);
    }
}
