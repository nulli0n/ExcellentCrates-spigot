package su.nightexpress.excellentcrates.api.crate.item;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public interface ICrateItemFactory {

    boolean hasValidItem(Crate crate);

    NightItem renderCrateIcon(Crate crate);

    ItemStack createDisplayItem(Crate crate);

    ItemStack createBaseItem(Crate crate);

    Optional<ItemStack> baseItem(Crate crate);
}
