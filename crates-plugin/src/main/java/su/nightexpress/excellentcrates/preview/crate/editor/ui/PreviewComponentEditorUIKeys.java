package su.nightexpress.excellentcrates.preview.crate.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.context.PreviewComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.context.PreviewComponentEditorMainMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class PreviewComponentEditorUIKeys {

    public static final MenuKey<PreviewComponentEditorMainMenuContext> MENU_MAIN = MenuKey.of(
        "preview.component.editor.main"
    );

    public static final DialogKey<PreviewComponentSelectionDialogContext> DIALOG_SELECTION = new DialogKey<>(
        "preview.component.editor.selection"
    );

    private PreviewComponentEditorUIKeys() {
    }
}
