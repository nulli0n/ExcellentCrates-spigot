package su.nightexpress.excellentcrates.reward.editor.ui.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommand;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.excellentcrates.reward.permission.RewardPerms;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

@NullMarked
public class RewardEditorCommand implements RewardCommand {

    private final RewardEditorUIService uiService;
    private final MessageDispatcher     dispatcher;

    public RewardEditorCommand(RewardEditorUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public LiteralNode createCommand() {
        return Commands.literal("editor", builder -> builder
            .description(RewardsLang.COMMAND_EDITOR_DESCRIPTION)
            .permission(RewardPerms.COMMAND_EDITOR)
            .playerOnly()
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();
        BackwardNavigator backwardNavigator = Player::closeInventory;

        return this.uiService.openBrowseMenu(player, backwardNavigator).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(player, locale, ctx);
        });
    }
}
