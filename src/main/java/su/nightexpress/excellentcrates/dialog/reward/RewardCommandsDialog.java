package su.nightexpress.excellentcrates.dialog.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.Plugins;

import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;
import static su.nightexpress.excellentcrates.Placeholders.*;

public class RewardCommandsDialog extends CrateDialog<CommandReward> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Reward.Commands.Title").text(title("Reward", "Commands"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Reward.Commands.Body").dialogElement(400,
        "Enter the " + SOFT_YELLOW.wrap("commands") + " to run when the reward is received in the Commands field.",
        "",
        SOFT_YELLOW.wrap("→") + " Use the " + SOFT_YELLOW.wrap(PLAYER_NAME) + " placeholder to insert the name of the player who received the reward.",
        "",
        SOFT_YELLOW.wrap("→") + " You can use the internal " + SOFT_YELLOW.wrap("Crate") + " and " + SOFT_YELLOW.wrap("Reward") + " placeholders: click " + OPEN_URL.with(WIKI_PLACEHOLDERS).wrap(SOFT_GREEN.and(UNDERLINED).wrap("HERE")) + " to view documentation.",
        "",
        SOFT_YELLOW.wrap("→") + " You can use any placeholders from the " + SOFT_YELLOW.wrap(Plugins.PLACEHOLDER_API) + " plugin."
    );

    private static final TextLocale INPUT_COMMANDS = LangEntry.builder("Dialog.Reward.Commands.Input.Commands").text("Commands");

    private static final String JSON_COMMANDS = "commands";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull CommandReward reward) {
        return Dialogs.create(builder -> {

            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.text(JSON_COMMANDS, INPUT_COMMANDS)
                        .initial(String.join("\n", reward.getCommands()))
                        .maxLength(400)
                        .width(300)
                        .multiline(new WrappedMultilineOptions(10, 150))
                        .build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String commandsRaw = nbtHolder.getText(JSON_COMMANDS).orElse(null);
                if (commandsRaw == null) return;

                reward.setCommands(List.of(String.join("\n", commandsRaw)));
                reward.getCrate().markDirty();
                viewer.callback();
            });
        });
    }
}
