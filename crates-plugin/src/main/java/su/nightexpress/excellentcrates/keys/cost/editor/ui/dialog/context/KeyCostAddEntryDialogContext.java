package su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;

@NullMarked
public record KeyCostAddEntryDialogContext(CrateReference crateRef,
                                           CrateEditorHook hook,
                                           Set<Identifier> existingKeys) {

}
