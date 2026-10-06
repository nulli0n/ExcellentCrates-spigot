package su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateItemSettingsMenuContext(Identifier crateId,
                                           CrateEditorHook hook,
                                           BackwardNavigator backwardNavigator) implements BackwardSupport {

}
