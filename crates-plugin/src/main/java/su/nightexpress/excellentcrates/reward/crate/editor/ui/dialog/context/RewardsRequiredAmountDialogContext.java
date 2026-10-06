package su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record RewardsRequiredAmountDialogContext(CrateReference crateRef, int currentAmount, CrateEditorHook hook) {

}
