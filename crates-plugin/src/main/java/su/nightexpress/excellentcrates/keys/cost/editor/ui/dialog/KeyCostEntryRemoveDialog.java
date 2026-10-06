package su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryRemoveDialogContext;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class KeyCostEntryRemoveDialog extends Dialog<KeyCostEntryRemoveDialogContext> {

    private final KeyCostEditorUIController uiController;

    public KeyCostEntryRemoveDialog(KeyCostEditorUIController uiController) {
        super();
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, KeyCostEntryRemoveDialogContext context) {
        Identifier keyId = context.keyId();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyCostLang.EDITOR_UI_DIALOG_REMOVE_ENTRY_TITLE)
                .body(DialogBodies.plain(KeyCostLang.EDITOR_UI_DIALOG_REMOVE_ENTRY_BODY).build())
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) {
                    viewer.close();
                    return;
                }

                Player clicker = viewer.getPlayer();
                if (this.uiController.onEntryRemoveDialogSubmit(clicker, crate, context.hook(), keyId)) {
                    viewer.callback();
                }
            });
        });
    }
}
