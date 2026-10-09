package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardPreviewDialogContext(RewardReference rewardRef, RewardPreview preview) {

}
