package su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardsRequiredAmountDialogContext;
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
public class RewardsRequiredAmountDialog extends Dialog<RewardsRequiredAmountDialogContext> {

    private static final String KEY_REQUIRED_AMOUNT = "required_amount";

    private final RewardComponentEditorUIController controller;

    public RewardsRequiredAmountDialog(RewardComponentEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardsRequiredAmountDialogContext context) {
        int currentAmount = context.currentAmount();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardComponentLang.EDITOR_UI_DIALOG_REQUIRED_AMOUNT_TITLE)
                .body(DialogBodies.plain(RewardComponentLang.EDITOR_UI_DIALOG_REQUIRED_AMOUNT_BODY).build())
                .inputs(DialogInputs.text(KEY_REQUIRED_AMOUNT,
                    RewardComponentLang.EDITOR_UI_DIALOG_REQUIRED_AMOUNT_INPUT_AMOUNT)
                    .initial(String.valueOf(currentAmount))
                    .maxLength(2)
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                int requiredAmount = nbtHolder.getInt(KEY_REQUIRED_AMOUNT, currentAmount);

                if (this.controller.onRequiredAmountDialogApply(player, context, requiredAmount)) {
                    viewer.callback();
                }
            });
        });
    }
}
