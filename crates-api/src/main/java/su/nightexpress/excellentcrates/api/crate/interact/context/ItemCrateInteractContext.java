package su.nightexpress.excellentcrates.api.crate.interact.context;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public record ItemCrateInteractContext(Player player, Crate crate, ItemStack item) implements CrateInteractContext {

}
