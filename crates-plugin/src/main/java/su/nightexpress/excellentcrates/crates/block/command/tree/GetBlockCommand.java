package su.nightexpress.excellentcrates.crates.block.command.tree;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
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
public class GetBlockCommand implements CrateCommand {

    private static final String ARG_CRATE = "crate";
    private static final String ARG_BLOCK = "block";

    private final BlockItemService       itemService;
    private final CrateMessageDispatcher dispatcher;

    public GetBlockCommand(BlockItemService itemService, CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("getblock", builder -> builder
            .permission(BlocksPerms.COMMAND_GET_BLOCK)
            .description(BlocksLang.COMMAND_GET_BLOCK_DESCRIPTION)
            .playerOnly()
            .withArguments(
                Arguments.argument(ARG_CRATE, Crate.class),
                Arguments.argument(ARG_BLOCK, CrateBlock.class)
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Crate crate = arguments.get(ARG_CRATE, Crate.class);
        CrateBlock block = arguments.get(ARG_BLOCK, CrateBlock.class);

        return this.dispatcher.handleFeedbackBase(player, crate,
            this.itemService.getBlockItem(player, crate, block)
        );
    }
}
