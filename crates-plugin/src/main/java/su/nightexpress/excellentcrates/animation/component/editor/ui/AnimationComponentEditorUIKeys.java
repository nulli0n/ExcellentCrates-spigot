package su.nightexpress.excellentcrates.animation.component.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.context.AnimationComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.animation.component.editor.ui.menu.context.AnimationComponentMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class AnimationComponentEditorUIKeys {

    public static final MenuKey<AnimationComponentMenuContext>              MENU_COMPONENT   = MenuKey.of(
        "animations.component.editor.component"
    );
    public static final DialogKey<AnimationComponentSelectionDialogContext> DIALOG_SELECTION = new DialogKey<>(
        "animations.component.editor.animation_selection"
    );

    private AnimationComponentEditorUIKeys() {
    }
}
