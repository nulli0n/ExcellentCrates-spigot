package su.nightexpress.excellentcrates.reward.crate.component.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.context.RewardRollCountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.context.RewardComponentSettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardComponentEditorUIKeys {

    public static final MenuKey<RewardComponentSettingsMenuContext> MENU_SETTINGS = MenuKey.of(
        "rewards.component.editor.settings"
    );

    public static final DialogKey<RewardRollCountDialogContext> DIALOG_ROLL_COUNT = new DialogKey<>(
        "rewards.component.editor.roll_count"
    );

    private RewardComponentEditorUIKeys() {
    }
}
