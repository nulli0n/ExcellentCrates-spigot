package su.nightexpress.excellentcrates.integration.itemsadder.block;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class ItemsAdderBlock implements CrateBlock {

    private final String     itemsAdderId;
    private final AdaptedKey key;

    public ItemsAdderBlock(String itemsAdderId, AdaptedKey key) {
        this.itemsAdderId = itemsAdderId;
        this.key = key;
    }

    @Override
    public Optional<ItemStack> getItemStack() {
        CustomStack customStack = CustomStack.getInstance(this.itemsAdderId);
        return customStack == null ? Optional.empty() : Optional.of(customStack.getItemStack());
    }

    @Override
    public void place(Location location, BlockFace blockFace) {
        CustomBlock.place(this.itemsAdderId, location);
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }
}
