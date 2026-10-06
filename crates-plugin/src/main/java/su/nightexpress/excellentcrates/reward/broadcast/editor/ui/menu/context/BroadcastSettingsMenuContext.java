package su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record BroadcastSettingsMenuContext(RewardReference rewardRef,
                                           RewardEditorHook hook,
                                           BackwardNavigator backwardNavigator) implements BackwardSupport {

}
