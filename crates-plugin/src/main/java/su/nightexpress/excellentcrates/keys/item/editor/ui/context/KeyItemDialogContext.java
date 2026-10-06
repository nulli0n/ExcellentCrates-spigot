package su.nightexpress.excellentcrates.keys.item.editor.ui.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public record KeyItemDialogContext(Identifier keyId, KeyEditorHook hook, ItemStack itemStack) {

}