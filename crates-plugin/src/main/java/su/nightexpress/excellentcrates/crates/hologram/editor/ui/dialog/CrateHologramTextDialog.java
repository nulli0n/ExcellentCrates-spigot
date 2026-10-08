package su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramTextDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
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
public class CrateHologramTextDialog extends Dialog<CrateHologramTextDialogContext> {

    private static final String KEY_TEXT = "text";

    private final CrateHologramEditorUIController controller;

    public CrateHologramTextDialog(CrateHologramEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, CrateHologramTextDialogContext context) {
        String currentText = String.join("\n", context.currentText());

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(HologramsLang.UI_DIALOG_HOLOGRAM_TEXT_TITLE)
                .body(DialogBodies.plain(HologramsLang.UI_DIALOG_HOLOGRAM_TEXT_BODY).build())
                .inputs(DialogInputs.text(KEY_TEXT, HologramsLang.UI_DIALOG_HOLOGRAM_TEXT_INPUT_TEXT)
                    .initial(currentText)
                    .maxLength(2048)
                    .width(320)
                    .multiline(new WrappedMultilineOptions(12, 144))
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Player clicker = viewer.getPlayer();

                String rawText = nbtHolder.getText(KEY_TEXT, currentText);
                List<String> newText = List.of(rawText.split("\n"));

                this.controller.onHologramTextDialogConfirm(clicker, context, newText);
                viewer.callback();
            });
        });
    }
}
