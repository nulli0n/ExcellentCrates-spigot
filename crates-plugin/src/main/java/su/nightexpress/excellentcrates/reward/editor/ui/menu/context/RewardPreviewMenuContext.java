package su.nightexpress.excellentcrates.reward.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record RewardPreviewMenuContext(Identifier rewardId,
                                       BackwardNavigator backwardNavigator) implements BackwardSupport {

}
