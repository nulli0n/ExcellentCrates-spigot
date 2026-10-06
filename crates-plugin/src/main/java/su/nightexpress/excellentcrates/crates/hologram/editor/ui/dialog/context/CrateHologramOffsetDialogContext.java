package su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateHologramOffsetDialogContext(CrateEditorHook hook,
                                               Identifier crateId,
                                               double currentX,
                                               double currentY,
                                               double currentZ) {

}
