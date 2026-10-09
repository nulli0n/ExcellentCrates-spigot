package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public record RewardCommandsMenuContext(RewardId rewardId,
                                        RewardEditorHook hook,
                                        BackwardNavigator backwardNavigator) implements BackwardSupport {

}
