package su.nightexpress.excellentcrates.reward.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardPreviewMenuContext(RewardReference rewardRef,
                                       BackwardNavigator backwardNavigator) implements BackwardSupport {

}
