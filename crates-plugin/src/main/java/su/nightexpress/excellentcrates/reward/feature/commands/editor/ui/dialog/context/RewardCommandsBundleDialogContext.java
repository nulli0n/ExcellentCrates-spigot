package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;

@NullMarked
public record RewardCommandsBundleDialogContext(Identifier rewardId,
                                                CommandBundleContext bundleContext,
                                                RewardEditorHook hook) {

}
