package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsGiveModeDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class RewardCommandsGiveModeDialog extends Dialog<RewardCommandsGiveModeDialogContext> {

    private static final String KEY_MODE = "mode";

    private final RewardCommandsEditorUIController controller;

    public RewardCommandsGiveModeDialog(RewardCommandsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardCommandsGiveModeDialogContext context) {
        RewardEditorHook hook = context.hook();
        RewardCommandExecutionMode currentMode = context.currentMode();

        List<WrappedSingleOptionEntry> options = new ArrayList<>();
        for (RewardCommandExecutionMode giveMode : RewardCommandExecutionMode.values()) {
            String key = LowerCase.INTERNAL.apply(giveMode.name());
            String localized = RewardCommandsLang.GIVE_MODE.getLocalized(giveMode);
            boolean current = giveMode == currentMode;

            options.add(new WrappedSingleOptionEntry(key, localized, current));
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardCommandsLang.UI_DIALOG_COMMANDS_GIVE_MODE_TITLE)
                .body(DialogBodies.plain(RewardCommandsLang.UI_DIALOG_COMMANDS_GIVE_MODE_BODY).build())
                .inputs(
                    DialogInputs.singleOption(
                        KEY_MODE,
                        RewardCommandsLang.UI_DIALOG_COMMANDS_GIVE_MODE_INPUT_MODE.text(),
                        options
                    ).build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String rawMode = nbtHolder.getText(KEY_MODE, currentMode.name());
                RewardCommandExecutionMode giveMode = Enums.parse(rawMode, RewardCommandExecutionMode.class).orElse(
                    currentMode);

                if (this.controller.onDialogCommandsGiveModeConfirm(player, hook, giveMode)) {
                    viewer.callback();
                }
            });
        });
    }
}
