package su.nightexpress.excellentcrates.keys.display.editor.ui.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public record KeyDisplayNameDialogContext(KeyEditorHook hook, String currentName) {

}
