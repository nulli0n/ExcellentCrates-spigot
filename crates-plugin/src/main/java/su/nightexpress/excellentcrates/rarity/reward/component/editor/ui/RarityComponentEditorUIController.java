package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.RarityComponentEditorService;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.context.RarityComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu.context.RarityComponentMainMenuContext;

@NullMarked
public class RarityComponentEditorUIController {

    private final RewardRegistry                 rewardRegistry;
    private final RarityComponentEditorService   editorService;
    private final RarityComponentEditorUIService uiService;
    private final RewardMessageDispatcher        dispatcher;

    public RarityComponentEditorUIController(RewardRegistry rewardRegistry,
                                             RarityComponentEditorService editorService,
                                             RarityComponentEditorUIService uiService,
                                             RewardMessageDispatcher dispatcher) {
        this.rewardRegistry = rewardRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Reward reward, RewardEditorHook hook,
                                 BackwardNavigator navigator) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RarityComponentMainMenuContext context = new RarityComponentMainMenuContext(rewardRef, hook, navigator);

        this.dispatcher.handleFeedbackBase(player, reward, this.uiService.openMainMenu(player, context));
    }

    public boolean onMainMenuStateClick(Player player, Reward reward, RewardEditorHook hook, boolean newState) {
        return this.dispatcher.handleFeedbackBase(player, reward, this.editorService.setComponentState(hook, newState));
    }

    public void onComponentMenuRarityClick(Player player, Reward reward, RewardEditorHook hook, Identifier currentKey,
                                           Runnable refreshUI) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RarityComponentSelectionDialogContext context = new RarityComponentSelectionDialogContext(
            rewardRef, hook, currentKey
        );

        this.dispatcher.handleFeedbackBase(player, reward,
            this.uiService.showSelectionDialog(player, context, refreshUI)
        );
    }

    public boolean onSelectionDialogRarityClick(Player player, Reward reward, RewardEditorHook hook,
                                                Identifier selectedId) {
        return this.dispatcher.handleFeedbackBase(player, reward,
            this.editorService.setComponentRarityId(hook, selectedId)
        );
    }
}
