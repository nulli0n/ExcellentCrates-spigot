package su.nightexpress.excellentcrates.keys.common.base.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu.context.KeyBaseMainMenuContext;

@NullMarked
public final class KeyBaseEditorUIKeys {

    public static final MenuKey<KeyBaseMainMenuContext> MAIN_MENU = MenuKey.of("keys.base.editor.main_menu");

    private KeyBaseEditorUIKeys() {
    }
}
