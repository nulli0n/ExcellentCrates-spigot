package su.nightexpress.excellentcrates.api.reward.editor;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;

@NullMarked
public interface RewardEditorAPI {

    void registerExtension(RewardEditorExtension extension);

    ActionResult openEditor(Player player, BackwardNavigator navigator);

    ActionResult openRewardOptions(Player player, Identifier rewardId, BackwardNavigator navigator);
}
