package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;

@NullMarked
public record RewardCommandsBundleDialogContext(RewardId rewardId,
                                                CommandBundleContext bundleContext,
                                                RewardEditorHook hook) {

}
