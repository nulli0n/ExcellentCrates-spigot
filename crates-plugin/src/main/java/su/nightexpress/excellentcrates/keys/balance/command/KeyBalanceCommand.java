package su.nightexpress.excellentcrates.keys.balance.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.balance.IKeyBalance;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.userdata.UserDataManager;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyBalanceCommand implements KeyCommand {

    private static final String ARGUMENT_PLAYER = "player";

    private final KeyRegistry          keyRegistry;
    private final KeyBalanceService    balanceService;
    private final UserDataManager      userDataService;
    private final KeyMessageDispatcher dispatcher;

    public KeyBalanceCommand(KeyRegistry keyRegistry,
                             KeyBalanceService balanceService,
                             UserDataManager userDataService,
                             KeyMessageDispatcher dispatcher) {
        this.keyRegistry = keyRegistry;
        this.balanceService = balanceService;
        this.userDataService = userDataService;
        this.dispatcher = dispatcher;
    }

    private record KeyBalanceResult(CrateKey key, IKeyBalance balance) {
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("balance", builder -> builder
            .description(KeyLang.COMMAND_BALANCE_DESCRIPTION)
            .permission(KeyPerms.COMMAND_BALANCE)
            .withArguments(Arguments.playerName(ARGUMENT_PLAYER)
                .optional()
                .permission(KeyPerms.COMMAND_BALANCE_OTHERS)
            )
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        CommandSender sender = context.getSender();
        String playerName = arguments.getString(ARGUMENT_PLAYER, sender.getName());

        this.userDataService.loadByNameAndCacheAsync(playerName).thenCompose(opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(sender, CoreLang.ERROR_INVALID_PLAYER);
                return CompletableFuture.completedFuture(null);
            }

            UserData userData = opt.get();

            List<CompletableFuture<KeyBalanceResult>> futures = new ArrayList<>();

            for (CrateKey key : this.keyRegistry.values()) {
                futures.add(this.balanceService.getKeyBalanceAsync(userData.getId(), key).thenApply(balance -> {
                    return new KeyBalanceResult(key, balance);
                }));
            }

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(v -> {
                    List<KeyBalanceResult> results = futures.stream()
                        .map(CompletableFuture::join)
                        .toList();
                    this.printKeyBalances(sender, userData, results);
                });
        });

        return true;
    }

    private void printKeyBalances(CommandSender sender, UserData userData, List<KeyBalanceResult> results) {
        List<KeyBalanceResult> sorted = results.stream()
            .filter(result -> !result.balance.isEmpty())
            .sorted(Comparator.comparing((KeyBalanceResult result) -> result.key().idString()))
            .toList();

        boolean self = sender.getName().equalsIgnoreCase(userData.getName());

        if (sorted.isEmpty()) {
            MessageLocale emptyMessage = self ? KeyLang.BALANCE_EMPTY_SELF : KeyLang.BALANCE_EMPTY_OTHERS;
            this.dispatcher.send(sender, emptyMessage, ctx -> ctx
                .with(CommonPlaceholders.PLAYER_NAME, userData::getName)
            );
            return;
        }

        MessageLocale header = self ? KeyLang.BALANCE_HEADER_SELF : KeyLang.BALANCE_HEADER_OTHERS;
        this.dispatcher.send(sender, header, ctx -> ctx
            .with(CommonPlaceholders.PLAYER_NAME, userData::getName)
        );

        sorted.forEach(result -> {
            CrateKey key = result.key();
            IKeyBalance balance = result.balance();

            this.dispatcher.sendBase(sender, key, KeyLang.BALANCE_ENTRY, ctx -> ctx
                .with("%inventory%", () -> NumberUtil.format(balance.physical()))
                .with("%virtual%", () -> NumberUtil.format(balance.virtual()))
                .with("%unclaimed%", () -> NumberUtil.format(balance.unclaimed()))
            );
        });
    }
}
