package su.nightexpress.excellentcrates.crates.item.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.item.editor.ui.dialog.context.CrateItemIconDialogContext;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context.CrateItemSettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateItemEditorUIKeys {

    public static final MenuKey<CrateItemSettingsMenuContext> MENU_SETTINGS = MenuKey.of(
        "crate.item.editor.settings"
    );

    public static final DialogKey<CrateItemIconDialogContext> DIALOG_ICON = new DialogKey<>(
        "crate.item.editor.icon"
    );

    private CrateItemEditorUIKeys() {
    }
}
