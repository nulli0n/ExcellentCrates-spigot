package su.nightexpress.excellentcrates.crates.open.editor.ui.dialog;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIController;
import su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.context.CrateOpeningCommandsDialogContext;
import su.nightexpress.excellentcrates.crates.open.lang.CrateOpeningLang;
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
public class CrateOpenActionsCommandsDialog extends Dialog<CrateOpeningCommandsDialogContext> {

    private static final String KEY_COMMANDS = "commands";

    private final CrateOpeningEditorUIController uiController;

    public CrateOpenActionsCommandsDialog(CrateOpeningEditorUIController uiController) {
        super();
        this.uiController = uiController;
    }

    @Override
    public WrappedDialog create(Player player, CrateOpeningCommandsDialogContext context) {
        CrateEditorHook hook = context.hook();
        List<String> currentCommands = context.currentCommands();
        String rawCurrentCommands = String.join("\n", currentCommands);

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(CrateOpeningLang.EDITOR_UI_DIALOG_COMMANDS_TITLE)
                .body(DialogBodies.plain(CrateOpeningLang.EDITOR_UI_DIALOG_COMMANDS_BODY).build())
                .inputs(DialogInputs.text(KEY_COMMANDS, CrateOpeningLang.EDITOR_UI_DIALOG_COMMANDS_INPUT_COMMANDS)
                    .initial(rawCurrentCommands)
                    .width(400)
                    .multiline(new WrappedMultilineOptions(10, 120))
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String commands = nbtHolder.getText(KEY_COMMANDS, rawCurrentCommands);
                List<String> newCommands = Stream.of(commands.split("\n"))
                    .filter(Predicate.not(String::isBlank))
                    .map(CrateOpenActionsCommandsDialog::validateCommand)
                    .toList();


                if (this.uiController.onCommandsDialogConfirm(player, hook, newCommands)) {
                    viewer.callback();
                }
            });
        });
    }

    private static String validateCommand(String command) {
        String trimmed = command.trim();

        if (trimmed.startsWith("/")) {
            return trimmed.substring(1);
        }

        return trimmed;
    }
}
