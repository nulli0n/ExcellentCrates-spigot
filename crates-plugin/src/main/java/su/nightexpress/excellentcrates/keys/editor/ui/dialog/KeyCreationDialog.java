package su.nightexpress.excellentcrates.keys.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.KeyConstants;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyCreationDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class KeyCreationDialog extends Dialog<KeyCreationDialogContext> {

    private static final String JSON_ID = "id";

    private final KeyEditorUIController controller;

    public KeyCreationDialog(KeyEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, KeyCreationDialogContext context) {
        String id = context.id();
        boolean conflictNotice = context.conflictNotice();

        List<WrappedDialogBody> bodies = new ArrayList<>();

        bodies.add(DialogBodies.plain(KeyEditorLang.UI_DIALOG_CREATION_BODY_MAIN).build());

        if (conflictNotice) {
            bodies.add(DialogBodies.plain(KeyEditorLang.UI_DIALOG_CREATION_BODY_ID_CONFLICT).build());
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyEditorLang.UI_DIALOG_CREATION_TITLE)
                .body(bodies)
                .inputs(DialogInputs.text(JSON_ID, KeyEditorLang.UI_DIALOG_CREATION_INPUT_ID.text())
                    .initial(id == null ? "" : id)
                    .maxLength(KeyConstants.KEY_ID_MAX_LENGTH)
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String finalId = nbtHolder.getText(JSON_ID).orElse(null);
                if (finalId == null || finalId.isBlank()) return;

                this.controller.onCreationDialogSubmit(player, finalId);
                viewer.callback();
            });
        });
    }
}
