package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.context;

import su.nightexpress.excellentcrates.api.common.limit.LimitSnapshot;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;

public record RewardLimitOptionsDialogContext(LimitType type, LimitSnapshot snapshot, RewardEditorHook hook) {

}
