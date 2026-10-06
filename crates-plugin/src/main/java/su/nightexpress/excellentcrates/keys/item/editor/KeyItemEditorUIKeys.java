package su.nightexpress.excellentcrates.keys.item.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.keys.item.editor.ui.menu.context.KeyItemMainMenuContext;

@NullMarked
public final class KeyItemEditorUIKeys {

    public static final MenuKey<KeyItemMainMenuContext> MENU_MAIN = MenuKey.of("keys.item.editor.main");

    private KeyItemEditorUIKeys() {
    }
}
