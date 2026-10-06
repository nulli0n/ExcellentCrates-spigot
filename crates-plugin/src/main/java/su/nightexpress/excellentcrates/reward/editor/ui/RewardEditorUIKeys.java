package su.nightexpress.excellentcrates.reward.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreateContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.PreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.BrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardEditorUIKeys {

    public static final MenuKey<BrowseMenuContext> MENU_BROWSE = MenuKey.of(
        "reward.editor.browse"
    );

    public static final MenuKey<RewardOptionsMenuContext> MENU_OPTIONS = MenuKey.of(
        "reward.editor.options"
    );

    public static final MenuKey<RewardPreviewMenuContext> MENU_PREVIEW = MenuKey.of(
        "reward.editor.preview"
    );

    public static final DialogKey<RewardCreateContext> DIALOG_CREATION = new DialogKey<>(
        "rewards.editor.creation"
    );

    public static final DialogKey<RewardDeletionDialogContext> DIALOG_DELETION = new DialogKey<>(
        "rewards.editor.deletion"
    );

    public static final DialogKey<PreviewDialogContext> DIALOG_PREVIEW_NAME = new DialogKey<>(
        "rewards.editor.preview.name"
    );

    public static final DialogKey<PreviewDialogContext> DIALOG_PREVIEW_LORE = new DialogKey<>(
        "rewards.editor.preview.lore"
    );

    private RewardEditorUIKeys() {
    }
}
