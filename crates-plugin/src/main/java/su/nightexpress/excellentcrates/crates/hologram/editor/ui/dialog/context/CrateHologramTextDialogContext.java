package su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateHologramTextDialogContext(CrateEditorHook hook, Identifier crateId, List<String> currentText) {

}
