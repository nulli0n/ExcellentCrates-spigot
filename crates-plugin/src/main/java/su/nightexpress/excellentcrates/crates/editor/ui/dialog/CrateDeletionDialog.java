package su.nightexpress.excellentcrates.crates.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateDeletionDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class CrateDeletionDialog extends Dialog<CrateDeletionDialogContext> {

    private final CrateEditorUIController controller;

    public CrateDeletionDialog(CrateEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, CrateDeletionDialogContext context) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(CrateEditorLang.UI_DIALOG_DELETE_CONFIRM_TITLE)
                .body(DialogBodies.plain(CrateEditorLang.UI_DIALOG_DELETE_CONFIRM_BODY).build())
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                Player clicker = viewer.getPlayer();
                Identifier crateId = context.crateId();
                BackwardNavigator backwardNavigator = context.backwardNavigator();

                this.controller.onDeletionDialogConfirm(clicker, crateId, backwardNavigator);
            });
        });
    }
}
