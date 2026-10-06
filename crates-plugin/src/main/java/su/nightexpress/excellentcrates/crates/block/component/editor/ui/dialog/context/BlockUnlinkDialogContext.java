package su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record BlockUnlinkDialogContext(Identifier crateId, CrateEditorHook hook) {

}
