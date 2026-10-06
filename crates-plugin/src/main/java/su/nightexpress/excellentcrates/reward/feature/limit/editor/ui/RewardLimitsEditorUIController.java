package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.common.limit.LimitSnapshot;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.RewardLimitsEditorService;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.context.RewardLimitOptionsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsAlternativeMenuContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsMainMenuContext;

@NullMarked
public class RewardLimitsEditorUIController implements FeedbackHandler {

    private final RewardLimitsEditorService   editorService;
    private final RewardLimitsEditorUIService uiService;
    private final MessageDispatcher           dispatcher;

    public RewardLimitsEditorUIController(RewardLimitsEditorService editorService,
                                          RewardLimitsEditorUIService uiService,
                                          MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier rewardId, RewardEditorHook hook,
                                 BackwardNavigator navigator) {
        RewardLimitsMainMenuContext context = new RewardLimitsMainMenuContext(rewardId, hook, navigator);

        this.handleFeedback(player, this.uiService.openMainLimitsMenu(player, context));
    }

    public boolean onLimitsMenuStateClick(Player player, RewardEditorHook hook, boolean newState) {
        return this.handleFeedback(player, this.editorService.setLimitsState(hook, newState));
    }

    public void onLimitsMenuLimitClick(Player player, RewardEditorHook hook, LimitType type, LimitSnapshot snapshot,
                                       Runnable refreshUI) {
        RewardLimitOptionsDialogContext context = new RewardLimitOptionsDialogContext(type, snapshot, hook);

        this.handleFeedback(player, this.uiService.openLimitOptionsDialog(player, context, refreshUI));
    }

    public boolean onLimitsMenuAlternativeStateClick(Player player, RewardEditorHook hook, boolean newState) {
        return this.handleFeedback(player, this.editorService.setAlternativeEnabled(hook, newState));
    }

    public void onLimitsMenuAlternativeRewardIdClick(Player player, RewardLimitsMainMenuContext currentContext) {
        Identifier rewardId = currentContext.rewardId();
        RewardEditorHook hook = currentContext.hook();
        BackwardNavigator backwardNavigator = user -> {
            this.handleFeedback(player, this.uiService.openMainLimitsMenu(player, currentContext));
        };

        RewardLimitsAlternativeMenuContext menuContext = new RewardLimitsAlternativeMenuContext(
            rewardId, hook, backwardNavigator
        );

        this.handleFeedback(player, this.uiService.openAlternativeSelectionMenu(player, menuContext));
    }

    public boolean onAlternativeMenuRewardClick(Player player, Identifier selectedId, RewardEditorHook hook) {
        return this.handleFeedback(player, this.editorService.setAlternativeRewardId(hook, selectedId));
    }

    public boolean onLimitOptionsDialogConfirm(Player player, LimitType type, LimitSnapshot newSnapshot,
                                               RewardEditorHook hook) {
        return this.handleFeedback(player, this.editorService.setLimitOptions(hook, type, newSnapshot));
    }
}
