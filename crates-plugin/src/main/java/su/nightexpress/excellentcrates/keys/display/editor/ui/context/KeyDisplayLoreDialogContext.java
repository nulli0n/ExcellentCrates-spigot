package su.nightexpress.excellentcrates.keys.display.editor.ui.context;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public record KeyDisplayLoreDialogContext(KeyEditorHook hook, List<String> currentLore) {

}
