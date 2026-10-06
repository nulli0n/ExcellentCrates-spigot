package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;

@NullMarked
public record RewardCommandsIterationsDialogContext(Identifier rewardId, int currentIterations, RewardEditorHook hook) {

}
