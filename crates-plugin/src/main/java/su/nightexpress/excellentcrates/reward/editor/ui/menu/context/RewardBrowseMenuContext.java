package su.nightexpress.excellentcrates.reward.editor.ui.menu.context;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;

@NullMarked
public record RewardBrowseMenuContext(AtomicBoolean quickMode,
                                      RewardComponentHook hook,
                                      CrateReference crateRef,
                                      BackwardNavigator backwardNavigator) implements BackwardSupport {

}
