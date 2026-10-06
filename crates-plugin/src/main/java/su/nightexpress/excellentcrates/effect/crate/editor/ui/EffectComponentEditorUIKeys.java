package su.nightexpress.excellentcrates.effect.crate.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog.context.EffectComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.menu.context.EffectComponentEditorMainMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class EffectComponentEditorUIKeys {

    public static final MenuKey<EffectComponentEditorMainMenuContext>    MENU_MAIN        = MenuKey.of(
        "effect.component.editor.main"
    );
    public static final DialogKey<EffectComponentSelectionDialogContext> DIALOG_SELECTION = new DialogKey<>(
        "effect.component.editor.selection"
    );

    private EffectComponentEditorUIKeys() {
    }
}
