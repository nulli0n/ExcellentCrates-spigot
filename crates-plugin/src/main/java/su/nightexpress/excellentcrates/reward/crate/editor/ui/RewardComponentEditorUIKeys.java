package su.nightexpress.excellentcrates.reward.crate.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntrySelectMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;
import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context.RewardsRequiredAmountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryBrowseMenuContext;

@NullMarked
public final class RewardComponentEditorUIKeys {

    public static final MenuKey<RewardEntryBrowseMenuContext> MENU_BROWSE = MenuKey.of(
        "rewards.component.editor.browse"
    );

    public static final MenuKey<RewardEntryOptionsMenuContext> MENU_OPTIONS = MenuKey.of(
        "rewards.component.editor.options"
    );

    public static final MenuKey<RewardEntrySelectMenuContext> MENU_SELECT = MenuKey.of(
        "rewards.component.editor.select"
    );

    public static final DialogKey<RewardWeightDialogContext> DIALOG_WEIGHT = new DialogKey<>(
        "rewards.component.editor.reward.weight"
    );

    public static final DialogKey<RewardsRequiredAmountDialogContext> DIALOG_REQUIRED_AMOUNT = new DialogKey<>(
        "rewards.component.editor.reward.amount"
    );

    private RewardComponentEditorUIKeys() {
    }
}
