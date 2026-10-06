package su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public record AnimationComponentSelectionDialogContext(CrateReference crateRef,
                                                       AdaptedKey currentKey,
                                                       CrateEditorHook hook) {

}
