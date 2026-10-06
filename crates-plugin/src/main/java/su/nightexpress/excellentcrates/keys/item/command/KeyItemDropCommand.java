package su.nightexpress.excellentcrates.keys.item.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.keys.item.KeyItemService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
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
public class KeyItemDropCommand implements KeyCommand {

    private static final String ARGUMENT_KEY    = "key";
    private static final String ARGUMENT_AMOUNT = "amount";
    private static final String ARGUMENT_WORLD  = "world";
    private static final String ARGUMENT_X      = "x";
    private static final String ARGUMENT_Y      = "y";
    private static final String ARGUMENT_Z      = "z";

    private final KeyItemService       itemService;
    private final KeyMessageDispatcher dispatcher;

    public KeyItemDropCommand(KeyItemService itemService, KeyMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("drop", builder -> builder
            .description(KeyLang.COMMAND_DROP_DESCRIPTION)
            .permission(KeyPerms.COMMAND_DROP)
            .withArguments(
                Arguments.argument(ARGUMENT_KEY, CrateKey.class),
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

        CrateKey key = arguments.get(ARGUMENT_KEY, CrateKey.class);
        int amount = arguments.getInt(ARGUMENT_AMOUNT, 1);

        World world = arguments.getWorld(ARGUMENT_WORLD);
        double x = arguments.getDouble(ARGUMENT_X);
        double y = arguments.getDouble(ARGUMENT_Y);
        double z = arguments.getDouble(ARGUMENT_Z);

        ItemStack itemStack = this.itemService.createTaggedItem(key).orElse(null);
        if (itemStack == null) {
            this.dispatcher.sendBase(sender, key, KeyLang.ERROR_ITEM_CREATION_FAILED);
            return false;
        }

        itemStack.setAmount(amount);

        Location location = new Location(world, x, y, z);
        world.dropItemNaturally(location, itemStack);

        this.dispatcher.sendBase(sender, key, KeyLang.KEY_DROP_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(amount))
            .with(SharedPlaceholders.LOCATION, () -> LocaleUtils.formatLocation(location))
            .with(SharedPlaceholders.WORLD, () -> BukkitThing.getAsString(world))
        );
        return true;
    }
}
