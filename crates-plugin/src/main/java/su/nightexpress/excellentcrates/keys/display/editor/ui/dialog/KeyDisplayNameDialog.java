package su.nightexpress.excellentcrates.keys.display.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayNameDialogContext;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;
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
public class KeyDisplayNameDialog extends Dialog<KeyDisplayNameDialogContext> {

    private static final String KEY_NAME = "name";

    private final KeyDisplayEditorUIController controller;

    public KeyDisplayNameDialog(KeyDisplayEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, KeyDisplayNameDialogContext context) {
        String currentName = context.currentName();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyDisplayLang.EDITOR_UI_DIALOG_NAME_TITLE)
                .body(DialogBodies.plain(KeyDisplayLang.EDITOR_UI_DIALOG_NAME_BODY).build())
                .inputs(
                    DialogInputs.text(KEY_NAME, KeyDisplayLang.EDITOR_UI_DIALOG_NAME_INPUT_NAME)
                        .initial(currentName)
                        .width(250)
                        .maxLength(400)
                        .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(KEY_NAME, currentName);

                this.controller.onDisplayNameDialogSubmit(player, context, name);
                viewer.callback();
            });
        });
    }
}
