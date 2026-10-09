package su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateDisplayDialogContext(Identifier crateId, CrateDisplay display, CrateEditorHook hook) {

}
