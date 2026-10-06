package su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record PreviewComponentEditorMainMenuContext(CrateReference crateRef,
                                                    CrateEditorHook hook,
                                                    BackwardNavigator backwardNavigator) implements BackwardSupport {

}
