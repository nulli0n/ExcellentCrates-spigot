package su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryAmountDialogContext;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class KeyCostEntryAmountDialog extends Dialog<KeyCostEntryAmountDialogContext> {

    private static final String JSON_AMOUNT = "amount";

    private final KeyCostEditorUIController controller;

    public KeyCostEntryAmountDialog(KeyCostEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, KeyCostEntryAmountDialogContext context) {
        CrateEditorHook hook = context.extensionContext();
        Identifier keyId = context.keyId();
        int currentAmount = context.currentAmount();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyCostLang.EDITOR_UI_DIALOG_ENTRY_AMOUNT_TITLE)
                .body(DialogBodies.plain(KeyCostLang.EDITOR_UI_DIALOG_ENTRY_AMOUNT_BODY).build())
                .inputs(
                    DialogInputs.text(JSON_AMOUNT, KeyCostLang.EDITOR_UI_DIALOG_ENTRY_AMOUNT_INPUT_AMOUNT)
                        .initial(String.valueOf(currentAmount))
                        .maxLength(8)
                        .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) {
                    viewer.close();
                    return;
                }

                Player clicker = viewer.getPlayer();
                int amount = nbtHolder.getInt(JSON_AMOUNT, currentAmount);

                if (this.controller.onEntryAmountDialogSubmit(clicker, crate, hook, keyId, amount)) {
                    viewer.callback();
                }
            });
        });
    }
}
