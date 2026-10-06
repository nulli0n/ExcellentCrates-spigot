package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RarityComponentSelectionDialogContext(RewardReference rewardRef,
                                                    RewardEditorHook hook,
                                                    Identifier currentId) {

}
