package su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record KeyCostEntryAmountDialogContext(CrateReference crateRef,
                                              CrateEditorHook extensionContext,
                                              Identifier keyId,
                                              int currentAmount) {

}
