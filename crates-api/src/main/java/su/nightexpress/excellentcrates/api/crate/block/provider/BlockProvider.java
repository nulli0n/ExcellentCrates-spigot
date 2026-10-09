package su.nightexpress.excellentcrates.api.crate.block.provider;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;

@NullMarked
public interface BlockProvider extends Identifiable {

    boolean canHandle(Location location);

    boolean isBlock(ItemStack itemStack);

    int getPriority();
}
