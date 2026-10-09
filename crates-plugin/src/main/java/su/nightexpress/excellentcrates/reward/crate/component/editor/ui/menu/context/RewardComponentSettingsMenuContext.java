package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record RewardComponentSettingsMenuContext(CrateEditorHook hook,
                                                 CrateReference crateRef,
                                                 BackwardNavigator backwardNavigator) implements BackwardSupport {

}
