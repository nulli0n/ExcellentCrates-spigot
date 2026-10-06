package su.nightexpress.excellentcrates.crates.item.editor.ui.dialog.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateItemIconDialogContext(Identifier crateId, ItemStack itemStack, CrateEditorHook hook) {

}
