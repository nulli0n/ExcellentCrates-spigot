package su.nightexpress.excellentcrates.crates.item.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;
import su.nightexpress.excellentcrates.crates.item.lang.CrateItemLang;
import su.nightexpress.excellentcrates.crates.item.permission.CrateItemPerms;
import su.nightexpress.excellentcrates.util.LocaleUtils;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateItemDropCommand implements CrateCommand, CrateFeedbackHandler {

    private static final String ARGUMENT_CRATE  = "crate";
    private static final String ARGUMENT_AMOUNT = "amount";
    private static final String ARGUMENT_WORLD  = "world";
    private static final String ARGUMENT_X      = "x";
    private static final String ARGUMENT_Y      = "y";
    private static final String ARGUMENT_Z      = "z";

    private final CrateItemService       itemService;
    private final CrateMessageDispatcher dispatcher;

    public CrateItemDropCommand(CrateItemService itemService, CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("drop", builder -> builder
            .description(CrateItemLang.COMMAND_DROP_DESCRIPTION)
            .permission(CrateItemPerms.COMMAND_DROP)
            .withArguments(
                Arguments.argument(ARGUMENT_CRATE, Crate.class),
                Arguments.integer(ARGUMENT_AMOUNT, 1)
                    .localized(CoreLang.COMMAND_ARGUMENT_NAME_AMOUNT)
                    .suggestions((reader, context) -> Lists.newList("1", "5", "10")),
                Arguments.world(ARGUMENT_WORLD),
                Arguments.decimal(ARGUMENT_X)
                    .localized(Lang.COMMAND_ARGUMENT_NAME_X)
                    .suggestions((reader, context) -> getCoords(context, Location::getBlockX)),
                Arguments.decimal(ARGUMENT_Y)
                    .localized(Lang.COMMAND_ARGUMENT_NAME_Y)
                    .suggestions((reader, context) -> getCoords(context, Location::getBlockY)),
                Arguments.decimal(ARGUMENT_Z)
                    .localized(Lang.COMMAND_ARGUMENT_NAME_Z)
                    .suggestions((reader, context) -> getCoords(context, Location::getBlockZ))
            )
            .executes(this::run)
        );
    }

    private List<String> getCoords(CommandContext context, Function<Location, Integer> function) {
        Player player = context.getPlayer();
        if (player == null) return Collections.emptyList();

        Location location = player.getLocation();
        return Lists.newList(String.valueOf(function.apply(location)));
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();

        Crate crate = arguments.get(ARGUMENT_CRATE, Crate.class);
        int amount = arguments.getInt(ARGUMENT_AMOUNT, 1);

        World world = arguments.getWorld(ARGUMENT_WORLD);
        double x = arguments.getDouble(ARGUMENT_X);
        double y = arguments.getDouble(ARGUMENT_Y);
        double z = arguments.getDouble(ARGUMENT_Z);

        ItemStack itemStack = this.itemService.createTaggedItem(crate);
        itemStack.setAmount(amount);

        Location location = new Location(world, x, y, z);
        world.dropItemNaturally(location, itemStack);

        return this.handleFeedbackBase(sender, crate, ActionResult.ok(CrateItemLang.COMMAND_DROP_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            .with(SharedPlaceholders.LOCATION, () -> LocaleUtils.formatLocation(location))
            .with(SharedPlaceholders.WORLD, () -> BukkitThing.getAsString(world))
        ));
    }
}
