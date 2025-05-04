package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.command.CommandArguments;
import su.nightexpress.excellentcrates.command.CommandFlags;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.OpenSettings;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.builder.DirectNodeBuilder;
import su.nightexpress.nightcore.command.experimental.impl.ReloadCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BaseCommands {

    public static void load(@NotNull CratesPlugin plugin) {
        var root = plugin.getRootNode();

        root.addChildren(ReloadCommand.builder(plugin, Perms.COMMAND_RELOAD));

        root.addChildren(ChainedNode.builder(plugin, "key")
            .description(Lang.COMMAND_KEY_DESC)
            .permission(Perms.COMMAND_KEY)
            .addDirect("drop", builder -> builder
                .description(Lang.COMMAND_DROP_KEY_DESC)
                .permission(Perms.COMMAND_DROP_KEY)
                .withArgument(CommandArguments.forKey(plugin).required())
                .withArgument(ArgumentTypes.decimal(CommandArguments.X).required().localized(Lang.COMMAND_ARGUMENT_NAME_X).withSamples(context -> getCoords(context, Location::getBlockX)))
                .withArgument(ArgumentTypes.decimal(CommandArguments.Y).required().localized(Lang.COMMAND_ARGUMENT_NAME_Y).withSamples(context -> getCoords(context, Location::getBlockY)))
                .withArgument(ArgumentTypes.decimal(CommandArguments.Z).required().localized(Lang.COMMAND_ARGUMENT_NAME_Z).withSamples(context -> getCoords(context, Location::getBlockZ)))
                .withArgument(ArgumentTypes.world(CommandArguments.WORLD).required())
                .executes((context, arguments) -> dropKey(plugin, context, arguments))
            )
            .addDirect("inspect", builder -> builder
                .description(Lang.COMMAND_KEY_INSPECT_DESC)
                .permission(Perms.COMMAND_KEY_INSPECT)
                .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).permission(Perms.COMMAND_KEY_INSPECT_OTHERS))
                .executes((context, arguments) -> inspectKeys(plugin, context, arguments))
            )
            .addDirect("giveall", builder -> builder
                .description(Lang.COMMAND_KEY_GIVE_ALL_DESC)
                .permission(Perms.COMMAND_KEY_GIVE)
                .withArgument(CommandArguments.forKey(plugin).required())
                .withArgument(ArgumentTypes.integerAbs(CommandArguments.AMOUNT).localized(Lang.COMMAND_ARGUMENT_NAME_AMOUNT).withSamples(context -> Lists.newList("1", "5", "10")))
                .withFlag(CommandFlags.silent())
                .withFlag(CommandFlags.silentFeedback())
                .executes((context, arguments) -> giveKeyAll(plugin, context, arguments))
            )
            .addDirect("give", builder -> buildKeyManage(plugin, builder)
                .description(Lang.COMMAND_KEY_GIVE_DESC)
                .permission(Perms.COMMAND_KEY_GIVE)
                .executes((context, arguments) -> giveKey(plugin, context, arguments))
            )
            .addDirect("set", builder -> buildKeyManage(plugin, builder)
                .description(Lang.COMMAND_KEY_SET_DESC)
                .permission(Perms.COMMAND_KEY_SET)
                .executes((context, arguments) -> setKey(plugin, context, arguments))
            )
            .addDirect("take", builder -> buildKeyManage(plugin, builder)
                .description(Lang.COMMAND_KEY_TAKE_DESC)
                .permission(Perms.COMMAND_KEY_TAKE)
                .executes((context, arguments) -> takeKey(plugin, context, arguments))
            )
        );

        root.addChildren(DirectNode.builder(plugin, "drop")
            .description(Lang.COMMAND_DROP_DESC)
            .permission(Perms.COMMAND_DROP)
            .withArgument(CommandArguments.forCrate(plugin).required())
            .withArgument(ArgumentTypes.decimal(CommandArguments.X).required().localized(Lang.COMMAND_ARGUMENT_NAME_X).withSamples(context -> getCoords(context, Location::getBlockX)))
            .withArgument(ArgumentTypes.decimal(CommandArguments.Y).required().localized(Lang.COMMAND_ARGUMENT_NAME_Y).withSamples(context -> getCoords(context, Location::getBlockY)))
            .withArgument(ArgumentTypes.decimal(CommandArguments.Z).required().localized(Lang.COMMAND_ARGUMENT_NAME_Z).withSamples(context -> getCoords(context, Location::getBlockZ)))
            .withArgument(ArgumentTypes.world(CommandArguments.WORLD).required())
            .executes((context, arguments) -> dropCrate(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "editor")
            .description(Lang.COMMAND_EDITOR_DESC)
            .permission(Perms.COMMAND_EDITOR)
            .playerOnly()
            .executes((context, arguments) -> openEditor(plugin, context))
        );

        root.addChildren(DirectNode.builder(plugin, "give")
            .description(Lang.COMMAND_GIVE_DESC)
            .permission(Perms.COMMAND_GIVE)
            .withArgument(ArgumentTypes.player(CommandArguments.PLAYER).required())
            .withArgument(CommandArguments.forCrate(plugin).required())
            .withArgument(ArgumentTypes.integerAbs(CommandArguments.AMOUNT).localized(Lang.COMMAND_ARGUMENT_NAME_AMOUNT).withSamples(context -> Lists.newList("1", "5", "10")))
            .withFlag(CommandFlags.silent())
            .withFlag(CommandFlags.silentFeedback())
            .executes((context, arguments) -> giveCrate(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "open")
            .description(Lang.COMMAND_OPEN_DESC)
            .permission(Perms.COMMAND_OPEN)
            .playerOnly()
            .withArgument(CommandArguments.forCrate(plugin).required())
            .executes((context, arguments) -> openCrate(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "openfor")
            .description(Lang.COMMAND_OPEN_FOR_DESC)
            .permission(Perms.COMMAND_OPEN_FOR)
            .withArgument(ArgumentTypes.player(CommandArguments.PLAYER).required())
            .withArgument(CommandArguments.forCrate(plugin).required())
            .withFlag(CommandFlags.silent())
            .withFlag(CommandFlags.force())
            .executes((context, arguments) -> openCrateFor(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "preview")
            .description(Lang.COMMAND_PREVIEW_DESC)
            .permission(Perms.COMMAND_PREVIEW)
            .withArgument(CommandArguments.forCrate(plugin).required())
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).permission(Perms.COMMAND_PREVIEW_OTHERS))
            .executes((context, arguments) -> previewCrate(plugin, context, arguments))
        );

        root.addChildren(DirectNode.builder(plugin, "resetcooldown")
            .description(Lang.COMMAND_RESET_COOLDOWN_DESC)
            .permission(Perms.COMMAND_RESETCOOLDOWN)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).required())
            .withArgument(CommandArguments.forCrate(plugin).required())
            .executes((context, arguments) -> resetCrateCooldown(plugin, context, arguments))
        );
    }

    @NotNull
    private static List<String> getCoords(@NotNull TabContext context, @NotNull Function<Location, Integer> function) {
        Player player = context.getPlayer();
        if (player == null) return Collections.emptyList();

        Location location = player.getLocation();
        return Lists.newList(String.valueOf(function.apply(location)));
    }

    @NotNull
    private static DirectNodeBuilder buildKeyManage(@NotNull CratesPlugin plugin, @NotNull DirectNodeBuilder builder) {
        return builder
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).required())
            .withArgument(CommandArguments.forKey(plugin).required())
            .withArgument(ArgumentTypes.integerAbs(CommandArguments.AMOUNT).localized(Lang.COMMAND_ARGUMENT_NAME_AMOUNT).withSamples(context -> Lists.newList("1", "5", "10")))
            .withFlag(CommandFlags.silent())
            .withFlag(CommandFlags.silentFeedback());
    }

    private static boolean dropCrate(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);

        double x = arguments.getDoubleArgument(CommandArguments.X);
        double y = arguments.getDoubleArgument(CommandArguments.Y);
        double z = arguments.getDoubleArgument(CommandArguments.Z);
        World world = arguments.getWorldArgument(CommandArguments.WORLD);

        Location location = new Location(world, x, y, z);

        if (!plugin.getCrateManager().dropCrateItem(crate, location)) return false;

        Lang.COMMAND_DROP_DONE.getMessage().send(context.getSender(), replacer -> replacer
            .replace(crate.replacePlaceholders())
            .replace(Placeholders.forLocation(location))
        );
        return true;
    }

    private static boolean openEditor(@NotNull CratesPlugin plugin, @NotNull CommandContext context) {
        plugin.getEditorManager().openEditor(context.getPlayerOrThrow());
        return true;
    }

    private static boolean giveCrate(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayerArgument(CommandArguments.PLAYER);
        Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);
        int amount = arguments.getIntArgument(CommandArguments.AMOUNT, 1);

        plugin.getCrateManager().giveCrateItem(player, crate, amount);

        if (!arguments.hasFlag(CommandFlags.SILENT)) {
            Lang.COMMAND_GIVE_NOTIFY.getMessage().send(player, replacer -> replacer
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(crate.replacePlaceholders())
            );
        }
        if (!arguments.hasFlag(CommandFlags.SILENT_FEEDBACK) && context.getSender() != player) {
            Lang.COMMAND_GIVE_DONE.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.forPlayer(player))
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(crate.replacePlaceholders())
            );
        }
        return true;
    }

    private static boolean openCrate(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);
        Player player = context.getPlayerOrThrow();
        plugin.getCrateManager().openCrate(player, new CrateSource(crate), new OpenSettings());
        return true;
    }

    private static boolean openCrateFor(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayerArgument(CommandArguments.PLAYER);
        Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);

        if (!arguments.hasFlag(CommandFlags.SILENT)) {
            Lang.COMMAND_OPEN_FOR_NOTIFY.getMessage().send(context.getSender(), replacer -> replacer.replace(crate.replacePlaceholders()));
        }
        if (context.getSender() != player) {
            Lang.COMMAND_OPEN_FOR_DONE.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.forPlayer(player))
                .replace(crate.replacePlaceholders())
            );
        }

        boolean force = arguments.hasFlag(CommandFlags.FORCE);
        plugin.getCrateManager().openCrate(player, new CrateSource(crate), new OpenSettings().setForce(force));
        return true;
    }

    private static boolean previewCrate(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);
        Player player = plugin.getServer().getPlayer(arguments.getStringArgument(CommandArguments.PLAYER, context.getSender().getName()));
        if (player == null) {
            context.errorBadPlayer();
            return false;
        }

        plugin.getCrateManager().previewCrate(player, new CrateSource(crate));

        if (context.getSender() != player) {
            Lang.COMMAND_PREVIEW_DONE_OTHERS.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.forPlayer(player))
                .replace(crate.replacePlaceholders())
            );
        }
        return true;
    }

    private static boolean resetCrateCooldown(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            Crate crate = arguments.getArgument(CommandArguments.CRATE, Crate.class);
            user.getCrateData(crate).setOpenCooldown(0);
            plugin.getUserManager().save(user);

            Lang.COMMAND_RESET_COOLDOWN_DONE.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(crate.replacePlaceholders())
            );
        });
        return true;
    }



    private static boolean dropKey(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        CrateKey key = arguments.getArgument(CommandArguments.KEY, CrateKey.class);

        double x = arguments.getDoubleArgument(CommandArguments.X);
        double y = arguments.getDoubleArgument(CommandArguments.Y);
        double z = arguments.getDoubleArgument(CommandArguments.Z);
        World world = arguments.getWorldArgument(CommandArguments.WORLD);

        Location location = new Location(world, x, y, z);

        if (!plugin.getKeyManager().dropKeyItem(key, location)) return false;

        Lang.COMMAND_DROP_KEY_DONE.getMessage().send(context.getSender(), replacer -> replacer
            .replace(key.replacePlaceholders())
            .replace(Placeholders.forLocation(location))
        );
        return true;
    }

    private static boolean giveKeyAll(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        CrateKey key = arguments.getArgument(CommandArguments.KEY, CrateKey.class);

        int amount = arguments.getIntArgument(CommandArguments.AMOUNT, 1);
        if (amount == 0) return false;

        boolean silent = arguments.hasFlag(CommandFlags.SILENT);

        Players.getOnline().forEach(player -> {
            if (!player.hasPermission(Perms.INCLUDE_KEY_GIVEALL)) return;

            plugin.getKeyManager().giveKey(player, key, amount);

            if (!silent) {
                Lang.COMMAND_KEY_GIVE_NOTIFY.getMessage().send(player, replacer -> replacer
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                );
            }
        });

        if (!arguments.hasFlag(CommandFlags.SILENT_FEEDBACK)) {
            Lang.COMMAND_KEY_GIVE_ALL_DONE.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(key.replacePlaceholders()));
        }
        return true;
    }

    private static boolean inspectKeys(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER, context.getSender().getName()), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            Lang.COMMAND_KEY_INSPECT_LIST.getMessage().send(context.getSender(), replacer -> replacer
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_ENTRY, list -> {
                    plugin.getKeyManager().getKeys().forEach(key -> {
                        if (!key.isVirtual()) return;

                        list.add(key.replacePlaceholders().apply(Lang.COMMAND_KEY_INSPECT_ENTRY.getString()
                            .replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(user.countKeys(key.getId())))
                        ));
                    });
                })
            );
        });
        return true;
    }

    private static boolean giveKey(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        CrateKey key = arguments.getArgument(CommandArguments.KEY, CrateKey.class);

        int amount = arguments.getIntArgument(CommandArguments.AMOUNT, 1);
        if (amount <= 0) return false;

        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            plugin.getKeyManager().giveKey(user, key, amount);

            Player target = user.getPlayer();
            if (target != null && !arguments.hasFlag(CommandFlags.SILENT)) {
                Lang.COMMAND_KEY_GIVE_NOTIFY.getMessage().send(target, replacer -> replacer
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                );
            }

            if (!arguments.hasFlag(CommandFlags.SILENT_FEEDBACK)) {
                Lang.COMMAND_KEY_GIVE_DONE.getMessage().send(context.getSender(), replacer -> replacer
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders()));
            }
        });
        return true;
    }

    private static boolean setKey(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        CrateKey key = arguments.getArgument(CommandArguments.KEY, CrateKey.class);

        int amount = arguments.getIntArgument(CommandArguments.AMOUNT, 1);
        if (amount <= 0) return false;

        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            plugin.getKeyManager().setKey(user, key, amount);

            Player target = user.getPlayer();
            if (target != null && !arguments.hasFlag(CommandFlags.SILENT)) {
                Lang.COMMAND_KEY_SET_NOTIFY.getMessage().send(target, replacer -> replacer
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                );
            }

            if (!arguments.hasFlag(CommandFlags.SILENT_FEEDBACK)) {
                Lang.COMMAND_KEY_SET_DONE.getMessage().send(context.getSender(), replacer -> replacer
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders()));
            }
        });
        return true;
    }

    private static boolean takeKey(@NotNull CratesPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        CrateKey key = arguments.getArgument(CommandArguments.KEY, CrateKey.class);

        int amount = arguments.getIntArgument(CommandArguments.AMOUNT, 1);
        if (amount <= 0) return false;

        plugin.getUserManager().manageUser(arguments.getStringArgument(CommandArguments.PLAYER), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            plugin.getKeyManager().takeKey(user, key, amount);

            Player target = user.getPlayer();
            if (target != null && !arguments.hasFlag(CommandFlags.SILENT)) {
                Lang.COMMAND_KEY_TAKE_NOTIFY.getMessage().send(target, replacer -> replacer
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders())
                );
            }

            if (!arguments.hasFlag(CommandFlags.SILENT_FEEDBACK)) {
                Lang.COMMAND_KEY_TAKE_DONE.getMessage().send(context.getSender(), replacer -> replacer
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, amount)
                    .replace(key.replacePlaceholders()));
            }
        });
        return true;
    }
}
