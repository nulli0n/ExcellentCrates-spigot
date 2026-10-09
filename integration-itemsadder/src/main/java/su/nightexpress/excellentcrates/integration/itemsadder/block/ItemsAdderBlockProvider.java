package su.nightexpress.excellentcrates.integration.itemsadder.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public class ItemsAdderBlockProvider implements BlockProvider {

    public final static Identifier ID = new Identifier("itemsadder");

    @Override
    public boolean canHandle(Location location) {
        Block block = location.getBlock();
        return CustomBlock.byAlreadyPlaced(block) != null || CustomFurniture.byAlreadySpawned(block) != null;
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        return CustomBlock.byItemStack(itemStack) != null || CustomFurniture.byItemStack(itemStack) != null;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
