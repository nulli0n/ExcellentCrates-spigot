package su.nightexpress.excellentcrates.keys.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyDeletionDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class KeyDeletionDialog extends Dialog<KeyDeletionDialogContext> {

    private final KeyEditorUIController controller;

    public KeyDeletionDialog(KeyEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, KeyDeletionDialogContext context) {
        Identifier keyId = context.keyId();

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(SharedPlaceholders.KEY_ID, () -> keyId.toString())
            .build();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyEditorLang.UI_DIALOG_DELETION_TITLE)
                .body(
                    //DialogBodies.item(context.currentIcon()).build(),
                    DialogBodies.plain(KeyEditorLang.UI_DIALOG_DELETION_BODY)
                        .placeholders(placeholders)
                        .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                Player clicker = viewer.getPlayer();

                if (this.controller.onDialogDeletionConfirmClick(clicker, keyId)) {
                    viewer.callback();
                }
            });
        });
    }

}
