package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;

@NullMarked
public record PreviewDialogContext(Identifier rewardId, RewardPreview preview) {

}
