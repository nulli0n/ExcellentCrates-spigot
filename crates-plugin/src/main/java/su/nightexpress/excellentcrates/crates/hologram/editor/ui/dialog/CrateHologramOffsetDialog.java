package su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.hologram.component.data.StandardHologramOffset;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramOffsetDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
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
public class CrateHologramOffsetDialog extends Dialog<CrateHologramOffsetDialogContext> {

    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_Z = "z";

    private final CrateHologramEditorUIController controller;

    public CrateHologramOffsetDialog(CrateHologramEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, CrateHologramOffsetDialogContext data) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(HologramsLang.UI_DIALOG_HOLOGRAM_OFFSET_TITLE)
                .body(DialogBodies.plain(HologramsLang.UI_DIALOG_HOLOGRAM_OFFSET_BODY).build())
                .inputs(
                    DialogInputs.text(KEY_X, HologramsLang.UI_DIALOG_HOLOGRAM_OFFSET_INPUT_X)
                        .initial(String.valueOf(data.currentX()))
                        .maxLength(3)
                        .build(),
                    DialogInputs.text(KEY_Y, HologramsLang.UI_DIALOG_HOLOGRAM_OFFSET_INPUT_Y)
                        .initial(String.valueOf(data.currentY()))
                        .maxLength(3)
                        .build(),
                    DialogInputs.text(KEY_Z, HologramsLang.UI_DIALOG_HOLOGRAM_OFFSET_INPUT_Z)
                        .initial(String.valueOf(data.currentZ()))
                        .maxLength(3)
                        .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Player clicker = viewer.getPlayer();

                double newX = nbtHolder.getDouble(KEY_X, data.currentX());
                double newY = nbtHolder.getDouble(KEY_Y, data.currentY());
                double newZ = nbtHolder.getDouble(KEY_Z, data.currentZ());

                StandardHologramOffset newOffset = new StandardHologramOffset(newX, newY, newZ);

                this.controller.onHologramOffsetDialogConfirm(clicker, data, newOffset);
                viewer.callback();
            });
        });
    }

}
