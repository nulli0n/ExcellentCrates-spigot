package su.nightexpress.excellentcrates.reward.items.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardItemsMenuContext(RewardReference rewardRef,
                                     RewardEditorHook hook,
                                     BackwardNavigator backwardNavigator) implements BackwardSupport {

}
