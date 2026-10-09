package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record RewardRollCountDialogContext(CrateEditorHook hook, CrateReference crateRef, int currentAmount) {

}
