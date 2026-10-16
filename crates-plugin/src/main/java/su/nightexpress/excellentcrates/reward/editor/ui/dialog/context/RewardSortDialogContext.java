package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;

@NullMarked
public record RewardSortDialogContext(RewardComponentHook hook, CrateReference crateRef) {

}
