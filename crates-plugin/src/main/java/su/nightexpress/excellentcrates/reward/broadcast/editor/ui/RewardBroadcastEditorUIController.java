package su.nightexpress.excellentcrates.reward.broadcast.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.broadcast.editor.RewardBroadcastEditorService;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.context.BroadcastSettingsMenuContext;

@NullMarked
public class RewardBroadcastEditorUIController {

    private final RewardRegistry                 rewardRegistry;
    private final RewardBroadcastEditorService   editorService;
    private final RewardBroadcastEditorUIService uiService;
    private final RewardMessageDispatcher        dispatcher;

    public RewardBroadcastEditorUIController(RewardRegistry rewardRegistry,
                                             RewardBroadcastEditorService editorService,
                                             RewardBroadcastEditorUIService uiService,
                                             RewardMessageDispatcher dispatcher) {
        this.rewardRegistry = rewardRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Reward reward, RewardEditorHook hook, BackwardNavigator navigator) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        BroadcastSettingsMenuContext context = new BroadcastSettingsMenuContext(
            rewardRef, hook, navigator
        );

        this.dispatcher.handleFeedback(player, this.uiService.openSettingsMenu(player, context));
    }

    public boolean onBaseMenuWinBroadcastClick(Player player, Reward reward, RewardEditorHook hook, boolean newState) {
        return this.dispatcher.handleFeedbackBase(player, reward,
            this.editorService.setWinBroadcastEnabled(hook, newState)
        );
    }
}
