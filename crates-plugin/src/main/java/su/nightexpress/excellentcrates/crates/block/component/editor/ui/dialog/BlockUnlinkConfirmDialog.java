package su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context.BlockUnlinkDialogContext;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class BlockUnlinkConfirmDialog extends Dialog<BlockUnlinkDialogContext> {

    private final BlockEditorUIController controller;

    public BlockUnlinkConfirmDialog(BlockEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, BlockUnlinkDialogContext context) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(BlocksLang.EDITOR_UI_DIALOG_UNLINK_TITLE)
                .body(DialogBodies.plain(BlocksLang.EDITOR_UI_DIALOG_UNLINK_CONTENT)
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                this.controller.onUnlinkDialogConfirmClick(player, context.crateId(), context.hook());
                viewer.callback();
            });
        });
    }
}
