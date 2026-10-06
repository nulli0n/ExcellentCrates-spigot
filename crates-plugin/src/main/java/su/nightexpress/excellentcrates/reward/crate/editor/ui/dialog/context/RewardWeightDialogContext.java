package su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardWeightDialogContext(CrateReference crateRef,
                                        RewardReference rewardRef,
                                        double weight,
                                        CrateEditorHook hook) {

}
