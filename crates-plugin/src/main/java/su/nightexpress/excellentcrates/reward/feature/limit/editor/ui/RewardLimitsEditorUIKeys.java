package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.context.RewardLimitOptionsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsAlternativeMenuContext;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsMainMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardLimitsEditorUIKeys {

    public static final MenuKey<RewardLimitsMainMenuContext> MENU_MAIN = MenuKey.of(
        "rewards.limits.editor.main_menu"
    );

    public static final MenuKey<RewardLimitsAlternativeMenuContext> MENU_ALTERNATIVE_SELECTION = MenuKey.of(
        "rewards.limits.editor.alternative_menu"
    );

    public static final DialogKey<RewardLimitOptionsDialogContext> DIALOG_OPTIONS = new DialogKey<>(
        "rewards.limits.editor.limit.options"
    );

    private RewardLimitsEditorUIKeys() {
    }
}
