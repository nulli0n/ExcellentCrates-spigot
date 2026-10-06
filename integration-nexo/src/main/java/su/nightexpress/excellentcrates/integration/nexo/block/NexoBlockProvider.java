package su.nightexpress.excellentcrates.integration.nexo.block;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public class NexoBlockProvider implements BlockProvider<NexoBlock> {

    public static final Identifier ID         = new Identifier("nexo");
    private static final KeyDomain KEY_DOMAIN = KeyDomain.of(ID.value());

    @Override
    public Identifier getId() {
        return ID;
    }

    /**
     * Fetches all Nexo blocks and furniture from the Nexo API and returns them as a set of {@link NexoBlock} instances.
     * This method is used to register the Nexo blocks with the ExcellentCrates block registry.
     */
    @Override
    public Set<NexoBlock> fetchBlocks() {
        Set<String> nexoBlockIds = new HashSet<>();

        nexoBlockIds.addAll(Arrays.asList(NexoBlocks.blockIDs()));
        nexoBlockIds.addAll(Arrays.asList(NexoFurniture.furnitureIDs()));

        return nexoBlockIds.stream()
            .map(id -> new NexoBlock(id, KEY_DOMAIN.make(id)))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean canHandle(Location location) {
        return NexoFurniture.isFurniture(location) || NexoBlocks.isCustomBlock(location.getBlock());
    }

    @Override
    public int getPriority() {
        return 10;
    }

}
