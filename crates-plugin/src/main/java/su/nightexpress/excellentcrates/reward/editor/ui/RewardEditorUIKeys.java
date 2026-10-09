package su.nightexpress.excellentcrates.reward.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardPreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardManualCreationDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardEditorUIKeys {

    public static final MenuKey<RewardBrowseMenuContext> MENU_BROWSE = MenuKey.of(
        "reward.editor.browse"
    );

    public static final MenuKey<RewardOptionsMenuContext> MENU_OPTIONS = MenuKey.of(
        "reward.editor.options"
    );

    public static final MenuKey<RewardPreviewMenuContext> MENU_PREVIEW = MenuKey.of(
        "reward.editor.preview"
    );

    public static final DialogKey<RewardManualCreationDialogContext> DIALOG_MANUAL_CREATION = new DialogKey<>(
        "rewards.editor.manual_creation"
    );

    public static final DialogKey<RewardDeletionDialogContext> DIALOG_DELETION = new DialogKey<>(
        "rewards.editor.deletion"
    );

    public static final DialogKey<RewardPreviewDialogContext> DIALOG_PREVIEW_NAME = new DialogKey<>(
        "rewards.editor.preview.name"
    );

    public static final DialogKey<RewardPreviewDialogContext> DIALOG_PREVIEW_LORE = new DialogKey<>(
        "rewards.editor.preview.lore"
    );

    public static final DialogKey<RewardWeightDialogContext> DIALOG_WEIGHT = new DialogKey<>(
        "rewards.component.editor.reward.weight"
    );

    private RewardEditorUIKeys() {
    }
}
