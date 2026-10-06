package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.CommandsBundleDeleteDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsBundleDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsGiveModeDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsIterationsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.context.RewardCommandsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardCommandsEditorUIKeys {

    public static final DialogKey<RewardCommandsIterationsDialogContext> DIALOG_ITERATIONS = new DialogKey<>(
        "rewards.editor.commands.iterations"
    );

    public static final DialogKey<RewardCommandsGiveModeDialogContext> DIALOG_GIVE_MODE = new DialogKey<>(
        "rewards.editor.commands.give_mode"
    );

    public static final DialogKey<RewardCommandsBundleDialogContext> DIALOG_BUNDLE = new DialogKey<>(
        "rewards.editor.commands.bundle"
    );

    public static final DialogKey<CommandsBundleDeleteDialogContext> DIALOG_BUNDLE_DELETE = new DialogKey<>(
        "rewards.editor.commands.bundle.delete"
    );

    public static final MenuKey<RewardCommandsMenuContext> MENU_COMMANDS = MenuKey.of(
        "reward.commands.editor.commands");

    private RewardCommandsEditorUIKeys() {
    }
}
