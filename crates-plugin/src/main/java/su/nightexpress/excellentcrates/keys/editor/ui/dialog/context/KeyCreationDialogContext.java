package su.nightexpress.excellentcrates.keys.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record KeyCreationDialogContext(@Nullable String id, boolean conflictNotice) {

}
