package su.nightexpress.excellentcrates.crates.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateCreationDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.context.CrateDeletionDialogContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateBrowseMenuContext;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateOptionsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateEditorUIKeys {

    public static final MenuKey<CrateBrowseMenuContext>  MENU_BROWSE  = MenuKey.of("crate.editor.browse");
    public static final MenuKey<CrateOptionsMenuContext> MENU_OPTIONS = MenuKey.of("crate.editor.options");

    public static final DialogKey<CrateCreationDialogContext> DIALOG_CREATION = new DialogKey<>(
        "crates.editor.creation"
    );

    public static final DialogKey<CrateDeletionDialogContext> DIALOG_DELETION = new DialogKey<>(
        "crates.editor.deletion"
    );

    private CrateEditorUIKeys() {
    }
}
