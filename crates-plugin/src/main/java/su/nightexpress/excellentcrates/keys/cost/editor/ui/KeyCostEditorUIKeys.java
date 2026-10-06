package su.nightexpress.excellentcrates.keys.cost.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostAddEntryDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryAmountDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryRemoveDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntriesMenuContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntryMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class KeyCostEditorUIKeys {

    public static final MenuKey<KeyCostEntriesMenuContext> MENU_ENTRIES = MenuKey.of(
        "keys.cost.editor.entries"
    );

    public static final MenuKey<KeyCostEntryMenuContext> MENU_ENTRY = MenuKey.of(
        "keys.cost.editor.entry"
    );

    public static final DialogKey<KeyCostAddEntryDialogContext> DIALOG_ADD_ENTRY = new DialogKey<>(
        "keys.cost.editor.add_entry"
    );

    public static final DialogKey<KeyCostEntryRemoveDialogContext> DIALOG_REMOVE_ENTRY = new DialogKey<>(
        "keys.cost.editor.remove_entry"
    );

    public static final DialogKey<KeyCostEntryAmountDialogContext> DIALOG_ENTRY_AMOUNT = new DialogKey<>(
        "keys.cost.editor.entry.amount"
    );

    private KeyCostEditorUIKeys() {
    }
}
