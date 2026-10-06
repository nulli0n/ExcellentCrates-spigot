package su.nightexpress.excellentcrates.crates.display.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.context.CrateDisplayDialogContext;
import su.nightexpress.excellentcrates.crates.display.editor.ui.menu.context.CrateDisplaySettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateDisplayEditorUIKeys {

    public static final MenuKey<CrateDisplaySettingsMenuContext> MENU_SETTINGS = MenuKey.of(
        "crate.display.editor.settings"
    );

    public static final DialogKey<CrateDisplayDialogContext> DIALOG_NAME = new DialogKey<>(
        "crates.display.editor.name"
    );

    public static final DialogKey<CrateDisplayDialogContext> DIALOG_LORE = new DialogKey<>(
        "crates.display.editor.lore"
    );


    private CrateDisplayEditorUIKeys() {
    }
}
