package su.nightexpress.excellentcrates.crates.item.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;
import su.nightexpress.excellentcrates.crates.item.lang.CrateItemLang;
import su.nightexpress.excellentcrates.crates.item.permission.CrateItemPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateItemGiveCommand implements CrateCommand, CrateFeedbackHandler {

    private static final String ARGUMENT_PLAYER = "player";
    private static final String ARGUMENT_CRATE  = "crate";
    private static final String ARGUMENT_AMOUNT = "amount";

    private final CrateItemService       itemService;
    private final CrateMessageDispatcher dispatcher;

    public CrateItemGiveCommand(CrateItemService itemService, CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("give", builder -> builder
            .description(CrateItemLang.COMMAND_GIVE_DESCRIPTION)
            .permission(CrateItemPerms.COMMAND_GIVE)
            .withArguments(
                Arguments.player(ARGUMENT_PLAYER),
                Arguments.argument(ARGUMENT_CRATE, Crate.class),
                Arguments.integer(ARGUMENT_AMOUNT, 1)
                    .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT)
                    .suggestions((reader, context) -> Lists.newList("1", "5", "10"))
                    .optional()
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();

        Player player = arguments.getPlayer(ARGUMENT_PLAYER);
        Crate crate = arguments.get(ARGUMENT_CRATE, Crate.class);
        int amount = arguments.getInt(ARGUMENT_AMOUNT, 1);

        ItemStack itemStack = this.itemService.createTaggedItem(crate);
        itemStack.setAmount(amount);
        Players.addItem(player, itemStack);

        return this.handleFeedbackBase(sender, crate, ActionResult.ok(CrateItemLang.COMMAND_GET_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            .with(CommonPlaceholders.PLAYER.resolver(player))
        ));
    }
}
