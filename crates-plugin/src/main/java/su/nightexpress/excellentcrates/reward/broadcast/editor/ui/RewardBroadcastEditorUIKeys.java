package su.nightexpress.excellentcrates.reward.broadcast.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.context.BroadcastSettingsMenuContext;

@NullMarked
public final class RewardBroadcastEditorUIKeys {

    public static final MenuKey<BroadcastSettingsMenuContext> MENU_SETTINGS = MenuKey.of(
        "rewards.broadcast.editor.settings"
    );

    private RewardBroadcastEditorUIKeys() {
    }
}
