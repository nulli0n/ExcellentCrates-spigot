package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.RewardCooldownsEditorService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.context.RewardCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu.context.RewardCooldownsMenuContext;

@NullMarked
public class RewardCooldownsEditorUIController {

    private final RewardRegistry                 rewardRegistry;
    private final RewardCooldownsEditorService   editorService;
    private final RewardCooldownsEditorUIService uiService;
    private final RewardMessageDispatcher        dispatcher;

    public RewardCooldownsEditorUIController(RewardRegistry rewardRegistry,
                                             RewardCooldownsEditorService editorService,
                                             RewardCooldownsEditorUIService uiService,
                                             RewardMessageDispatcher dispatcher) {
        this.rewardRegistry = rewardRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Reward reward, RewardEditorHook hook, BackwardNavigator navigator) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RewardCooldownsMenuContext context = new RewardCooldownsMenuContext(rewardRef, hook, navigator);

        this.dispatcher.handleFeedbackBase(player, reward, this.uiService.openCooldownsMenu(player, context));
    }

    public void onCooldownsMenuCooldownClick(Player player, RewardCooldownsMenuContext context,
                                             CooldownType type,
                                             CooldownSnapshot snapshot,
                                             Runnable refreshUI) {
        RewardReference rewardRef = context.rewardRef();
        RewardEditorHook hook = context.hook();

        Reward reward = rewardRef.get();
        if (reward == null) return;

        RewardCooldownsSettingsDialogContext dialogContext = new RewardCooldownsSettingsDialogContext(
            type, snapshot, rewardRef, hook
        );

        this.dispatcher.handleFeedbackBase(player, reward,
            this.uiService.showCooldownSettingsDialog(player, dialogContext, refreshUI)
        );
    }

    public boolean onCooldownsSettingsDialogSubmit(Player player, RewardCooldownsSettingsDialogContext context,
                                                   CooldownSnapshot newSnapshot) {
        Reward reward = context.rewardRef().get();
        RewardEditorHook hook = context.hook();

        return reward != null && this.dispatcher.handleFeedbackBase(player, reward,
            this.editorService.editCooldown(hook, context.type(), newSnapshot)
        );
    }
}
