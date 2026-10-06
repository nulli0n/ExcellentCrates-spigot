package su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardEntryOptionsMenuContext(CrateReference crateRef,
                                            RewardReference rewardRef,
                                            CrateEditorHook hook,
                                            BackwardNavigator backwardNavigator) implements BackwardSupport {

}
