package su.nightexpress.excellentcrates.reward.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;

@NullMarked
public record RewardOptionsMenuContext(RewardComponentHook hook,
                                       CrateReference crateRef,
                                       RewardReference rewardRef,
                                       BackwardNavigator backwardNavigator) implements BackwardSupport {

}
