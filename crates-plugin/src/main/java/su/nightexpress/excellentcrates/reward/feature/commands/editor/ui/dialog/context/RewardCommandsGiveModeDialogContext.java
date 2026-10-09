package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public record RewardCommandsGiveModeDialogContext(RewardId rewardId,
                                                  RewardCommandExecutionMode currentMode,
                                                  RewardEditorHook hook) {

}
