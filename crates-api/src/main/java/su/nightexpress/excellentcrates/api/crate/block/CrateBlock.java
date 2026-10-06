package su.nightexpress.excellentcrates.api.crate.block;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.key.KeyHolder;

@NullMarked
public interface CrateBlock extends KeyHolder {

    Optional<ItemStack> getItemStack();

    void place(Location location, BlockFace face);
}
