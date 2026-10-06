package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;

@NullMarked
public record RewardLimitsMainMenuContext(Identifier rewardId,
                                          RewardEditorHook hook,
                                          BackwardNavigator backwardNavigator) implements BackwardSupport {

}
