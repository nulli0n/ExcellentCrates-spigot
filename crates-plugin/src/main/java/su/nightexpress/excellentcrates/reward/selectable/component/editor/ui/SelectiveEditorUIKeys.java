package su.nightexpress.excellentcrates.reward.selectable.component.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu.context.SelectiveEditorSettingsMenuContext;

@NullMarked
public final class SelectiveEditorUIKeys {

    public static final MenuKey<SelectiveEditorSettingsMenuContext> SETTINGS = MenuKey.of(
        "rewards.selective.settings"
    );

    private SelectiveEditorUIKeys() {
    }
}
