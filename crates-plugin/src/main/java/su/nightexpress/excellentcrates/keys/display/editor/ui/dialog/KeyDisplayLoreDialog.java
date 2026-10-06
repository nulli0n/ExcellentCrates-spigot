package su.nightexpress.excellentcrates.keys.display.editor.ui.dialog;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayLoreDialogContext;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class KeyDisplayLoreDialog extends Dialog<KeyDisplayLoreDialogContext> {

    private static final String KEY_LORE = "lore";

    private final KeyDisplayEditorUIController controller;

    public KeyDisplayLoreDialog(KeyDisplayEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, KeyDisplayLoreDialogContext context) {
        List<String> currentLore = context.currentLore();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(KeyDisplayLang.EDITOR_UI_DIALOG_LORE_TITLE)
                .body(DialogBodies.plain(KeyDisplayLang.EDITOR_UI_DIALOG_LORE_BODY).build())
                .inputs(
                    DialogInputs.text(KEY_LORE, KeyDisplayLang.EDITOR_UI_DIALOG_LORE_INPUT_LORE)
                        .initial(String.join("\n", currentLore))
                        .maxLength(600)
                        .width(300)
                        .multiline(new WrappedMultilineOptions(10, 150))
                        .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                List<String> lore = nbtHolder.getText(KEY_LORE)
                    .map(str -> Arrays.asList(str.split("\n")))
                    .orElse(currentLore);

                this.controller.onDisplayLoreDialogSubmit(player, context, lore);
                viewer.callback();
            });
        });
    }
}
