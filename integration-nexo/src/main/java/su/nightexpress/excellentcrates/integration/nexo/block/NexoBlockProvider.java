package su.nightexpress.excellentcrates.integration.nexo.block;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public class NexoBlockProvider implements BlockProvider {

    public static final Identifier ID = new Identifier("nexo");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public boolean canHandle(Location location) {
        return NexoFurniture.isFurniture(location) || NexoBlocks.isCustomBlock(location.getBlock());
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        return NexoFurniture.isFurniture(itemStack) || NexoBlocks.isCustomBlock(itemStack);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
