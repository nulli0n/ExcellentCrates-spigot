package su.nightexpress.excellentcrates.reward.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class RewardDeletionDialog extends Dialog<RewardDeletionDialogContext> {

    private final RewardEditorUIController controller;

    public RewardDeletionDialog(RewardEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardDeletionDialogContext context) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardEditorLang.UI_DIALOG_DELETION_TITLE)
                .body(
                    DialogBodies.item(context.icon().getItemStack()).build(),
                    DialogBodies.plain(RewardEditorLang.UI_DIALOG_DELETION_BODY).build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                Player clicker = viewer.getPlayer();

                Reward reward = context.rewardRef().get();
                if (reward == null) return;

                Crate crate = context.crateRef().get();
                if (crate == null) return;

                if (this.controller.onDialogDeletionConfirmClick(clicker, context.hook(), crate, reward)) {
                    viewer.callback();
                }
            });
        });
    }
}
