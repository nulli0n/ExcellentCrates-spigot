package su.nightexpress.excellentcrates.reward.editor;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;

@NullMarked
public class DefaultRewardsEditorAPI implements RewardEditorAPI {

    private final TinyRegistry<RewardEditorExtension> extensions;
    //private final RewardEditorService editorService;
    private final RewardEditorUIService uiService;

    public DefaultRewardsEditorAPI(TinyRegistry<RewardEditorExtension> extensions,
                                   RewardEditorService editorService,
                                   RewardEditorUIService uiService) {
        this.extensions = extensions;
        //this.editorService = editorService;
        this.uiService = uiService;
    }

    @Override
    public ActionResult openEditor(Player player, BackwardNavigator navigator) {
        return this.uiService.openBrowseMenu(player, navigator);
    }

    @Override
    public ActionResult openRewardOptions(Player player, Identifier rewardId, BackwardNavigator navigator) {
        return this.uiService.openOptionsMenu(player, rewardId, navigator);
    }

    @Override
    public void registerExtension(RewardEditorExtension extension) {
        this.extensions.register(extension);
    }
}
