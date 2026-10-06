package su.nightexpress.excellentcrates.reward.crate.editor.ui;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.crate.editor.RewardComponentEditorService;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardsRequiredAmountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntrySelectMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;

@NullMarked
public class RewardComponentEditorUIController {

    private final CrateRegistry                  crateRegistry;
    private final RewardRegistry                 rewardRegistry;
    private final RewardEditorUIService          editorUIService;
    private final RewardComponentEditorService   editorService;
    private final RewardComponentEditorUIService uiService;
    private final CrateMessageDispatcher         dispatcher;

    public RewardComponentEditorUIController(CrateRegistry crateRegistry,
                                             RewardRegistry rewardRegistry,
                                             RewardEditorUIService editorUIService,
                                             RewardComponentEditorService editorService,
                                             RewardComponentEditorUIService uiService,
                                             CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.rewardRegistry = rewardRegistry;
        this.editorUIService = editorUIService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        RewardEntryBrowseMenuContext menuContext = new RewardEntryBrowseMenuContext(crateRef, hook, navigator);

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openRewardsMenu(player, menuContext));
    }

    public void onBrowseMenuEditorClick(Player player, RewardEntryBrowseMenuContext currentContext) {
        Crate crate = currentContext.crateRef().get();
        if (crate == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate, this.uiService.openRewardsMenu(user, currentContext));
        };

        this.dispatcher.handleFeedbackBase(player, crate, this.editorUIService.openBrowseMenu(player, navigator));
    }

    public void onBrowseMenuRequiredAmountClick(Player player, RewardEntryBrowseMenuContext currentContext,
                                                int currentAmount,
                                                Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();

        Crate crate = crateRef.get();
        if (crate == null) return;

        RewardsRequiredAmountDialogContext context = new RewardsRequiredAmountDialogContext(
            crateRef, currentAmount, hook
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showRewardsAmountDialog(player, context, refreshUI)
        );
    }

    public void onBrowseMenuAddClick(Player player, RewardEntryBrowseMenuContext currentContext, Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();

        Crate crate = crateRef.get();
        if (crate == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate,
                this.uiService.openRewardsMenu(user, currentContext)
            );
        };

        Consumer<Identifier> onSelect = selectedId -> {
            this.dispatcher.handleFeedbackBase(player, crate,
                this.editorService.addExistingReward(hook, selectedId)
            );
            navigator.moveBack(player);
        };

        // null rewardId, because we want to add new reward to a crate, not replace id of existing one.
        RewardEntrySelectMenuContext selectionContext = new RewardEntrySelectMenuContext(
            crateRef, null, onSelect, navigator
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.openRewardSelectionMenu(player, selectionContext)
        );
    }

    public void onBrowseMenuRewardClick(Player player, RewardEntryBrowseMenuContext currentContext, Reward reward) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();

        Crate crate = crateRef.get();
        if (crate == null) return;

        RewardReference rewardRef = this.rewardRegistry.createReference(reward);

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate,
                this.uiService.openRewardsMenu(user, currentContext)
            );
        };

        RewardEntryOptionsMenuContext context = new RewardEntryOptionsMenuContext(crateRef, rewardRef, hook, navigator);
        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.openRewardOptionsMenu(player, context)
        );
    }

    public void onOptionsIdClick(Player player, RewardEntryOptionsMenuContext currentContext, Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();
        RewardReference rewardRef = currentContext.rewardRef();

        Crate crate = crateRef.get();
        if (crate == null) return;

        Reward reward = rewardRef.get();
        if (reward == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate,
                this.uiService.openRewardOptionsMenu(user, currentContext)
            );
        };

        Consumer<Identifier> onSelect = selectedId -> {
            this.editorService.setCrateRewardId(hook, reward.id(), selectedId);
            refreshUI.run();
        };

        RewardEntrySelectMenuContext selectionContext = new RewardEntrySelectMenuContext(
            crateRef, rewardRef, onSelect, navigator
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.openRewardSelectionMenu(player, selectionContext)
        );
    }

    public void onOptionsWeightClick(Player player, RewardEntryOptionsMenuContext currentContext, double currentWeight,
                                     Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();
        RewardReference rewardRef = currentContext.rewardRef();

        Crate crate = crateRef.get();
        if (crate == null) return;

        RewardWeightDialogContext dialogContext = new RewardWeightDialogContext(
            crateRef, rewardRef, currentWeight, hook
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showRewardWeightDialog(player, dialogContext, refreshUI)
        );
    }

    public void onOptionsEditorClick(Player player, RewardEntryOptionsMenuContext currentContext) {
        CrateReference crateRef = currentContext.crateRef();
        RewardReference rewardRef = currentContext.rewardRef();

        Crate crate = crateRef.get();
        if (crate == null) return;

        Reward reward = rewardRef.get();
        if (reward == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate,
                this.uiService.openRewardOptionsMenu(user, currentContext)
            );
        };

        this.dispatcher.handleFeedbackBase(player, crate,
            this.editorUIService.openOptionsMenu(player, reward.id(), navigator)
        );
    }

    public boolean onWeightDialogApply(Player player, RewardWeightDialogContext context, double weight) {
        Crate crate = context.crateRef().get();
        CrateEditorHook hook = context.hook();
        RewardReference rewardRef = context.rewardRef();

        Reward reward = rewardRef.get();
        if (reward == null) return false;

        return crate != null && this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setCrateRewardWeight(hook, reward.id(), weight)
        );
    }

    public boolean onRequiredAmountDialogApply(Player player, RewardsRequiredAmountDialogContext context,
                                               int requiredAmount) {
        Crate crate = context.crateRef().get();
        CrateEditorHook hook = context.hook();

        return crate != null && this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setRequiredAmount(hook, requiredAmount)
        );
    }
}
