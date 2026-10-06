package su.nightexpress.excellentcrates.api.crate.block.provider;

import java.util.Set;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;

@NullMarked
public interface BlockProvider<B extends CrateBlock> extends Identifiable {

    /**
     * Fetches all blocks provided by this provider.
     *
     * @return a set of all blocks provided by this provider.
     */
    Set<B> fetchBlocks();

    boolean canHandle(Location location);

    int getPriority();
}
