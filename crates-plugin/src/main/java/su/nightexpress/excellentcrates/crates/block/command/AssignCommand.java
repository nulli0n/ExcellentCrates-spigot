package su.nightexpress.excellentcrates.crates.block.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.excellentcrates.crates.block.permission.BlocksPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class AssignCommand implements CrateCommand {

    private static final String ARG_CRATE = "crate";

    private final BlockItemService       itemService;
    private final CrateMessageDispatcher dispatcher;

    public AssignCommand(BlockItemService itemService, CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("assign", builder -> builder
            .permission(BlocksPerms.COMMAND_ASSIGN)
            .description(BlocksLang.COMMAND_ASSIGN_DESCRIPTION)
            .playerOnly()
            .withArguments(
                Arguments.argument(ARG_CRATE, Crate.class)
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Crate crate = arguments.get(ARG_CRATE, Crate.class);

        ActionResult result = this.itemService.assignCrateToBlockInHand(player, crate);
        return this.dispatcher.handleFeedbackBase(player, crate, result);
    }
}
