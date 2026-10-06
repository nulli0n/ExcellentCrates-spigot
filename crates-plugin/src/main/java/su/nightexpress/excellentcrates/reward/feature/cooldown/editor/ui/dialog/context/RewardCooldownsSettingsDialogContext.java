package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardCooldownsSettingsDialogContext(CooldownType type,
                                                   CooldownSnapshot snapshot,
                                                   RewardReference rewardRef,
                                                   RewardEditorHook hook) {

}
