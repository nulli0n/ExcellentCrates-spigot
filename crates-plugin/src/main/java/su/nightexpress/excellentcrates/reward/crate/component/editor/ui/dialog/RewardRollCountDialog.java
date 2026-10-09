package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.context.RewardRollCountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
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
public class RewardRollCountDialog extends Dialog<RewardRollCountDialogContext> {

    private static final String KEY_ROLL_COUNT = "roll_count";

    private final RewardComponentEditorUIController controller;

    public RewardRollCountDialog(RewardComponentEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardRollCountDialogContext context) {
        int currentAmount = context.currentAmount();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardComponentLang.EDITOR_UI_DIALOG_ROLL_COUNT_TITLE)
                .body(DialogBodies.plain(RewardComponentLang.EDITOR_UI_DIALOG_ROLL_COUNT_BODY).build())
                .inputs(DialogInputs.text(KEY_ROLL_COUNT,
                    RewardComponentLang.EDITOR_UI_DIALOG_ROLL_COUNT_INPUT_AMOUNT)
                    .initial(String.valueOf(currentAmount))
                    .maxLength(2)
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                int requiredAmount = nbtHolder.getInt(KEY_ROLL_COUNT, currentAmount);

                if (this.controller.onRollCountDialogSubmit(player, context, requiredAmount)) {
                    viewer.callback();
                }
            });
        });
    }
}
