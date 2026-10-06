package su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public record PreviewComponentSelectionDialogContext(CrateReference crateRef,
                                                     AdaptedKey currentKey,
                                                     CrateEditorHook hook) {

}
