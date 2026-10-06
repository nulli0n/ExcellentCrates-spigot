package su.nightexpress.excellentcrates.integration.nexo.block;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;

import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class NexoBlock implements CrateBlock {

    private final String     nexoId;
    private final AdaptedKey key;

    public NexoBlock(String nexoId, AdaptedKey key) {
        this.nexoId = nexoId;
        this.key = key;
    }

    @Override
    public Optional<ItemStack> getItemStack() {
        ItemBuilder builder = NexoItems.itemFromId(this.nexoId);
        return builder == null ? Optional.empty() : Optional.of(builder.build());
    }

    @Override
    public void place(Location location, BlockFace face) {
        NexoBlocks.place(this.nexoId, location);
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }
}
