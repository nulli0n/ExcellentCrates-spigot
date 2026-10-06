package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsIterationsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
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
public class RewardCommandsIterationsDialog extends Dialog<RewardCommandsIterationsDialogContext> {

    private static final String KEY_ITERATIONS = "iterations";

    private final RewardCommandsEditorUIController controller;

    public RewardCommandsIterationsDialog(RewardCommandsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardCommandsIterationsDialogContext context) {
        RewardEditorHook hook = context.hook();
        int currentIterations = context.currentIterations();
        String amountInputLabel = RewardCommandsLang.UI_DIALOG_COMMANDS_ITERATIONS_INPUT_AMOUNT.text();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardCommandsLang.UI_DIALOG_COMMANDS_ITERATIONS_TITLE)
                .body(DialogBodies.plain(RewardCommandsLang.UI_DIALOG_COMMANDS_ITERATIONS_BODY).build())
                .inputs(
                    DialogInputs.text(KEY_ITERATIONS, amountInputLabel)
                        .initial(String.valueOf(currentIterations))
                        .maxLength(2)
                        .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                int iterations = nbtHolder.getInt(KEY_ITERATIONS, currentIterations);

                if (this.controller.onDialogCommandsIterationsConfirm(player, hook, iterations)) {
                    viewer.callback();
                }
            });
        });
    }

}
