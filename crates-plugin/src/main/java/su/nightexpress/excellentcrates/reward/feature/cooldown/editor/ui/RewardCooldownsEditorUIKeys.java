package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.context.RewardCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu.context.RewardCooldownsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardCooldownsEditorUIKeys {

    public static final MenuKey<RewardCooldownsMenuContext>             MENU_COOLDOWNS = MenuKey.of(
        "rewards.cooldowns.editor.cooldowns"
    );
    public static final DialogKey<RewardCooldownsSettingsDialogContext> DIALOG_OPTIONS = new DialogKey<>(
        "rewards.cooldowns.editor.cooldown.settings"
    );

    private RewardCooldownsEditorUIKeys() {
    }
}
