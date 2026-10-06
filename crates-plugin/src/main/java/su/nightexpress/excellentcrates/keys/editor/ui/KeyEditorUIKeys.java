package su.nightexpress.excellentcrates.keys.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyCreationDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.context.KeyDeletionDialogContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeyBrowseMenuContext;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeySettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class KeyEditorUIKeys {

    public static final MenuKey<KeyBrowseMenuContext> MENU_BROWSE = MenuKey.of("keys.editor.browse");

    public static final MenuKey<KeySettingsMenuContext> MENU_SETTINGS = MenuKey.of("keys.editor.settings");

    public static final DialogKey<KeyCreationDialogContext> DIALOG_CREATION = new DialogKey<>("keys.editor.creation");

    public static final DialogKey<KeyDeletionDialogContext> DIALOG_DELETION = new DialogKey<>("keys.editor.deletion");

    private KeyEditorUIKeys() {
    }
}
