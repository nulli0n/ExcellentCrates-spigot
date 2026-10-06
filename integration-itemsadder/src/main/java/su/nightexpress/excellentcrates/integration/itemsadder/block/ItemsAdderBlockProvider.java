package su.nightexpress.excellentcrates.integration.itemsadder.block;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public class ItemsAdderBlockProvider implements BlockProvider<ItemsAdderBlock> {

    public final static Identifier ID     = new Identifier("itemsadder");
    private final static KeyDomain DOMAIN = KeyDomain.of(ID.value());

    @Override
    public Set<ItemsAdderBlock> fetchBlocks() {
        Set<String> itemsAdderBlockIds = new HashSet<>();

        itemsAdderBlockIds.addAll(CustomBlock.getNamespacedIdsInRegistry());
        itemsAdderBlockIds.addAll(CustomFurniture.getNamespacedIdsInRegistry());

        return itemsAdderBlockIds.stream()
            .map(id -> new ItemsAdderBlock(id, DOMAIN.make(id)))
            .collect(Collectors.toSet());
    }


    @Override
    public boolean canHandle(Location location) {
        Block block = location.getBlock();
        return CustomBlock.byAlreadyPlaced(block) != null || CustomFurniture.byAlreadySpawned(block) != null;
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
