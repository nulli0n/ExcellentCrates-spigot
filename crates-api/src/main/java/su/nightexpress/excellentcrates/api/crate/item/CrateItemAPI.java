package su.nightexpress.excellentcrates.api.crate.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateItemAPI {

    ItemStack createTaggedItem(Crate crate);

    @Nullable
    Identifier getCrateId(ItemStack item);

    boolean isCrateItem(ItemStack item);

    boolean isCrateItem(ItemStack item, Crate crate);
}
