package su.nightexpress.excellentcrates.keys.balance.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class KeyRedeemCommand implements KeyCommand {

    private final KeyRegistry          keyRegistry;
    private final KeyBalanceService    balanceService;
    private final KeyMessageDispatcher dispatcher;

    public KeyRedeemCommand(KeyRegistry keyRegistry,
                            KeyBalanceService balanceService,
                            KeyMessageDispatcher dispatcher) {
        this.keyRegistry = keyRegistry;
        this.balanceService = balanceService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("redeem", builder -> builder
            .playerOnly()
            .description(KeyLang.COMMAND_REDEEM_DESCRIPTION)
            .permission(KeyPerms.COMMAND_REDEEM)
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();
        List<CrateKey> unclaimedKeys = new ArrayList<>();

        this.keyRegistry.values().forEach(key -> {
            if (this.balanceService.countUnclaimedKeys(player, key) > 0) {
                unclaimedKeys.add(key);
            }
        });

        if (unclaimedKeys.isEmpty()) {
            this.dispatcher.send(player, KeyLang.KEY_REDEEM_NO_KEYS);
            return false;
        }

        unclaimedKeys.forEach(key -> {
            ActionResult result = this.balanceService.redeemKey(player, key);
            result.handleFeedback((locale, actionContext) -> {
                this.dispatcher.sendBase(player, key, locale, actionContext);
            });
        });

        return true;
    }
}
