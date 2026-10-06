package su.nightexpress.excellentcrates.reward.selectable.ui;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickContext;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickService;
import su.nightexpress.excellentcrates.reward.selectable.ui.menu.context.RewardSelectionMenuContext;

@NullMarked
public class SelectiveUIController {

    private final CrateRegistry           crateRegistry;
    private final SelectiveUIService      uiService;
    private final SelectivePickService    pickService;
    private final RewardMessageDispatcher dispatcher;

    public SelectiveUIController(CrateRegistry crateRegistry,
                                 SelectiveUIService uiService,
                                 SelectivePickService pickService,
                                 RewardMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.uiService = uiService;
        this.pickService = pickService;
        this.dispatcher = dispatcher;
    }

    public boolean startSelection(Player player, Crate crate, int requiredRewards,
                                  ProcessCallback<SelectivePickContext> callback) {
        CrateReference crateRef = this.crateRegistry.createReference(crate.id());

        SelectivePickContext pickContext = new SelectivePickContext(requiredRewards, new ArrayList<>());
        Runnable onAbort = callback::cancel;
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        RewardSelectionMenuContext menuContext = new RewardSelectionMenuContext(crateRef, pickContext, isCompleted,
            onAbort,
            pick -> {
                ActionResult testPick = this.pickService.validatePick(player, crate, pick);
                if (testPick.failure()) {
                    testPick.handleFeedback((locale, ctx) -> this.dispatcher.sendBase(player, crate, locale, ctx));
                    return;
                }

                isCompleted.set(true);
                player.closeInventory();

                callback.proceed(pick);
            });

        return this.uiService.openSelectionMenu(player, menuContext).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, crate, locale, ctx);
        });
    }

    public boolean onSelectionRewardPick(Player player, Crate crate, Reward reward, SelectivePickContext context) {
        ActionResult result = this.pickService.pickReward(player, crate, reward, context);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.sendAll(player, crate, reward, locale, ctx));
    }

    public boolean onSelectionRewardUnpick(Player player, Crate crate, Reward reward, SelectivePickContext context) {
        ActionResult result = this.pickService.unpickReward(player, crate, reward, context);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.sendAll(player, crate, reward, locale, ctx));
    }
}
