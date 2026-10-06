package su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateHologramOptionsMenuContext(CrateEditorHook hook,
                                              Identifier crateId,
                                              BackwardNavigator backwardNavigator) implements BackwardSupport {

}
