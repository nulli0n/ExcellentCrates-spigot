package su.nightexpress.excellentcrates.crates.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateCreationDialogContext;
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
public class CrateCreationDialog extends Dialog<CrateCreationDialogContext> {

    private static final String JSON_ID = "id";

    private final CrateEditorUIController controller;

    public CrateCreationDialog(CrateEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, CrateCreationDialogContext context) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(CrateEditorLang.UI_DIALOG_CREATION_TITLE)
                .body(DialogBodies.plain(CrateEditorLang.UI_DIALOG_CREATION_BODY).build())
                .inputs(DialogInputs
                    .text(JSON_ID, CrateEditorLang.UI_DIALOG_CREATION_INPUT_ID)
                    .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));
            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_ID).orElse(null);
                if (name == null) return;

                this.controller.onCreationDialogApply(player, name);
                viewer.callback();
            });
        });
    }
}
