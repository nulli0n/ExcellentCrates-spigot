package su.nightexpress.excellentcrates.crates.cooldown.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.dialog.context.CrateCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.menu.context.CrateCooldownsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateCooldownsEditorUIKeys {

    public static final MenuKey<CrateCooldownsMenuContext>             MENU_COOLDOWNS           = MenuKey.of(
        "crates.cooldowns.editor.cooldowns"
    );
    public static final DialogKey<CrateCooldownsSettingsDialogContext> DIALOG_COOLDOWN_SETTINGS = new DialogKey<>(
        "crates.cooldowns.editor.cooldown.settings"
    );

    private CrateCooldownsEditorUIKeys() {
    }
}
