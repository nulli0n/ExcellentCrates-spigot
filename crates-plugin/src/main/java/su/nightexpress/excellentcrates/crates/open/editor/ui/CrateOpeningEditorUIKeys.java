package su.nightexpress.excellentcrates.crates.open.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.context.CrateOpeningCommandsDialogContext;
import su.nightexpress.excellentcrates.crates.open.editor.ui.menu.context.CrateOpeningSettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateOpeningEditorUIKeys {

    public static final MenuKey<CrateOpeningSettingsMenuContext>     MENU_SETTINGS   = MenuKey.of(
        "crate.opening.editor.settings"
    );
    public static final DialogKey<CrateOpeningCommandsDialogContext> DIALOG_COMMANDS = new DialogKey<>(
        "crates.opening.editor.commands"
    );

    private CrateOpeningEditorUIKeys() {
    }
}
