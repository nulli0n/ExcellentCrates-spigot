package su.nightexpress.excellentcrates.crates.display.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context.CrateDisplayDialogContext;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
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
public class CrateDisplayNameDialog extends Dialog<CrateDisplayDialogContext> {

    private static final String JSON_NAME = "name";

    private final DisplayEditorUIController controller;

    public CrateDisplayNameDialog(DisplayEditorUIController controller) {
        super();
        this.controller = controller;
    }

    public WrappedDialog create(Player player, CrateDisplayDialogContext context) {
        // Identifier crateId = context.crateId();
        ICrateDisplay display = context.display();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(CrateEditorLang.UI_DIALOG_DISPLAY_NAME_TITLE)
                .body(DialogBodies.plain(CrateEditorLang.UI_DIALOG_DISPLAY_NAME_BODY).build())
                .inputs(
                    DialogInputs.text(JSON_NAME, CrateEditorLang.UI_DIALOG_DISPLAY_NAME_INPUT_NAME)
                        .initial(display.getName())
                        .width(250)
                        .maxLength(400)
                        .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_NAME, display.getName());

                this.controller.onSettingsNameDialogApply(player, context.hook(), name);
                viewer.callback();
            });
        });
    }
}
