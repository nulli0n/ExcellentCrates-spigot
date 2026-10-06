package su.nightexpress.excellentcrates.keys.display.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayLoreDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.context.KeyDisplayNameDialogContext;
import su.nightexpress.excellentcrates.keys.display.editor.ui.menu.context.KeyDisplayMainMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class KeyDisplayEditorUIKeys {

    public static final MenuKey<KeyDisplayMainMenuContext> MENU_MAIN = MenuKey.of(
        "keys.display.editor.main"
    );

    public static final DialogKey<KeyDisplayNameDialogContext> DIALOG_NAME = new DialogKey<>(
        "keys.display.editor.name"
    );

    public static final DialogKey<KeyDisplayLoreDialogContext> DIALOG_LORE = new DialogKey<>(
        "keys.display.editor.lore"
    );

    private KeyDisplayEditorUIKeys() {
    }
}
