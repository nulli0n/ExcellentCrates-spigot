package su.nightexpress.excellentcrates.crates.block.vanilla.block;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class VanillaBlock implements CrateBlock {

    private final AdaptedKey             key;
    private final VanillaBlockDefinition definition;

    public VanillaBlock(AdaptedKey key, VanillaBlockDefinition definition) {
        this.key = key;
        this.definition = definition;
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }

    @Override
    public Optional<ItemStack> getItemStack() {
        return Optional.of(new ItemStack(this.definition.getItemType()));
    }

    @Override
    public void place(Location location, BlockFace face) {
        BlockData data = this.definition.getBlockType().createBlockData();
        if (data instanceof Directional directional) {
            directional.setFacing(face);
        }

        location.getBlock().setBlockData(data, false);
    }
}
