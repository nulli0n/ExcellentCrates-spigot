package su.nightexpress.excellentcrates.keys.editor.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record KeyCreationContext(Identifier id, @Nullable ItemStack itemStack) {

}
