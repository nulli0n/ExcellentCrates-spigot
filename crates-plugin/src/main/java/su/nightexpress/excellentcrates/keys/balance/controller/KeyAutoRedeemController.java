package su.nightexpress.excellentcrates.keys.balance.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;

@NullMarked
public class KeyAutoRedeemController extends BaseController {

    private final KeyRegistry          keyRegistry;
    private final KeyBalanceService    balanceService;
    private final KeyMessageDispatcher dispatcher;

    public KeyAutoRedeemController(CratesPlugin plugin,
                                   KeyRegistry keyRegistry,
                                   KeyBalanceService balanceService,
                                   KeyMessageDispatcher dispatcher) {
        super(plugin);
        this.keyRegistry = keyRegistry;
        this.balanceService = balanceService;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onControllerReload() {

    }

    @Override
    protected void onControllerShutdown() {

    }

    @Override
    protected void onControllerStart() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(KeyPerms.AUTO_REDEEM)) {
            this.redeemKeys(player);
        }
    }

    private void redeemKeys(Player player) {
        this.keyRegistry.values().forEach(key -> {
            if (this.balanceService.countUnclaimedKeys(player, key) > 0) {
                ActionResult result = this.balanceService.redeemKey(player, key);
                result.handleFeedback((locale, actionContext) -> {
                    this.dispatcher.sendBase(player, key, locale, actionContext);
                });
            }
        });
    }
}
