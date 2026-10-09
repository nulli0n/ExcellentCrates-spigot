package su.nightexpress.excellentcrates.crates.display.editor.ui.dialog;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context.CrateDisplayDialogContext;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
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
public class CrateDisplayLoreDialog extends Dialog<CrateDisplayDialogContext> {

    private static final String JSON_LORE = "lore";

    private final DisplayEditorUIController controller;

    public CrateDisplayLoreDialog(DisplayEditorUIController controller) {
        super();
        this.controller = controller;
    }

    public WrappedDialog create(Player player, CrateDisplayDialogContext context) {
        // Identifier crateId = context.crateId();
        CrateDisplay display = context.display();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(CrateEditorLang.UI_DIALOG_DISPLAY_LORE_TITLE)
                .body(DialogBodies.plain(CrateEditorLang.UI_DIALOG_DISPLAY_LORE_BODY).build())
                .inputs(
                    DialogInputs.text(JSON_LORE, CrateEditorLang.UI_DIALOG_DISPLAY_LORE_INPUT_LORE)
                        .initial(String.join("\n", display.getLore()))
                        .maxLength(600)
                        .width(300)
                        .multiline(new WrappedMultilineOptions(10, 150))
                        .build()
                )
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                List<String> lore = nbtHolder.getText(JSON_LORE)
                    .map(str -> Arrays.asList(str.split("\n")))
                    .orElse(display.getLore());

                this.controller.onSettingsLoreDialogApply(player, context.hook(), lore);
                viewer.callback();
            });
        });
    }
}