package su.nightexpress.excellentcrates.keys.balance.command;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyGiveCommand implements KeyCommand {

    private static final String ARGUMENT_PLAYER = "player";
    private static final String ARGUMENT_KEY    = "key";
    private static final String ARGUMENT_AMOUNT = "amount";

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyGiveCommand.class);

    private final KeyBalanceService    balanceService;
    private final UserDataManager      userDataService;
    private final KeyMessageDispatcher dispatcher;

    public KeyGiveCommand(KeyBalanceService balanceService,
                          UserDataManager userDataService,
                          KeyMessageDispatcher dispatcher) {
        this.balanceService = balanceService;
        this.userDataService = userDataService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("give", builder -> builder
            .description(KeyLang.COMMAND_GIVE_DESCRIPTION)
            .permission(KeyPerms.COMMAND_GIVE)
            .withArguments(
                Arguments.playerName(ARGUMENT_PLAYER),
                Arguments.argument(ARGUMENT_KEY, CrateKey.class),
                Arguments.integer(ARGUMENT_AMOUNT, 1)
                    .optional()
                    .suggestions((reader, context) -> List.of("1", "5", "10"))
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        String playerName = arguments.getString(ARGUMENT_PLAYER);
        CrateKey key = arguments.get(ARGUMENT_KEY, CrateKey.class);
        int amount = arguments.getInt(ARGUMENT_AMOUNT, 1);
        CommandSender sender = context.getSender();

        this.userDataService.loadByNameAndCacheAsync(playerName).thenCompose(opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(sender, CoreLang.ERROR_INVALID_PLAYER);
                return CompletableFuture.completedFuture(null);
            }

            UserData userData = opt.get();

            return this.balanceService.giveKeyAsync(userData.getId(), key, amount).thenAccept(result -> {
                result.handleFeedback((locale, actionContext) -> {
                    this.dispatcher.sendBase(sender, key, locale, builder -> builder
                        .apply(actionContext)
                        .with(CommonPlaceholders.PLAYER_NAME, userData::getName)
                    );

                    if (result.success()) {
                        Players.findById(userData.getId()).ifPresent(target -> {
                            this.dispatcher.sendBase(target, key, KeyLang.KEY_GIVE_NOTIFY, actionContext);
                        });
                    }
                });
            });
        }).exceptionally(exception -> {
            LOGGER.error("An error occurred while giving key", exception);
            this.dispatcher.send(sender, Lang.GENERIC_INTERNAL_ERROR);
            return null;
        });

        return true;
    }
}
