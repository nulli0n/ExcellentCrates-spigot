package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsBundleDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
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
public class RewardCommandsBundleDialog extends Dialog<RewardCommandsBundleDialogContext> {

    private static final String KEY_COMMANDS = "commands";
    private static final String KEY_WEIGHT   = "weight";

    private final RewardCommandsEditorUIController controller;

    public RewardCommandsBundleDialog(RewardCommandsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardCommandsBundleDialogContext context) {
        RewardEditorHook hook = context.hook();

        CommandBundleContext bundleContext = context.bundleContext();
        UUID bundleId = bundleContext.id();
        boolean supportsWeight = bundleContext.supportsWeight();
        double weight = bundleContext.weight();
        List<String> commands = bundleContext.commands();

        List<WrappedDialogBody> bodies = new ArrayList<>();
        List<WrappedDialogInput> inputs = new ArrayList<>();

        bodies.add(DialogBodies.plain(RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_BODY_MAIN).build());
        inputs.add(DialogInputs.text(KEY_COMMANDS, RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_INPUT_COMMANDS.text())
            .initial(String.join("\n", commands))
            .multiline(new WrappedMultilineOptions(5, 60))
            .width(280)
            .maxLength(512)
            .build()
        );

        if (supportsWeight) {
            bodies.add(DialogBodies.plain(RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_BODY_WEIGHT).build());
            inputs.add(DialogInputs.text(KEY_WEIGHT, RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_INPUT_WEIGHT.text())
                .initial(String.valueOf(weight))
                .maxLength(4)
                .build()
            );
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_TITLE)
                .body(bodies)
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                double newWeight = supportsWeight ? nbtHolder.getDouble(KEY_WEIGHT, weight) : weight;
                List<String> newCommands = Stream.of(nbtHolder.getText(KEY_COMMANDS, "").split("\n"))
                    .filter(Predicate.not(String::isBlank))
                    .map(command -> {
                        if (command.startsWith("/")) command = command.substring(1);
                        return command;
                    })
                    .toList();

                CommandBundleContext newBundleContext = new CommandBundleContext(bundleId, supportsWeight, newWeight,
                    newCommands);

                if (this.controller.onDialogCommandsBundleConfirm(player, hook, newBundleContext)) {
                    viewer.callback();
                }
            });
        });
    }

}
