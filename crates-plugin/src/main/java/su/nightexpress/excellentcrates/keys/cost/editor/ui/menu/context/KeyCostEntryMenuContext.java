package su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record KeyCostEntryMenuContext(CrateEditorHook hook,
                                      CrateReference crateRef,
                                      Identifier keyId,
                                      BackwardNavigator backwardNavigator) implements BackwardSupport {

}
