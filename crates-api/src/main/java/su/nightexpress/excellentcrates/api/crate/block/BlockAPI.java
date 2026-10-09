package su.nightexpress.excellentcrates.api.crate.block;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionObserver;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;

@NullMarked
public interface BlockAPI {

    ActionResult handlePlacement(Identifier source, Player player, ItemStack itemInHand, Location location);

    ActionResult handleRemoval(Identifier source, Player player, Location location);

    ActionResult handleInteraction(Identifier sourceId, BlockInteractionType type, Player player, Location location);

    void addObserver(CratePositionObserver observer);

    void removeObserver(CratePositionObserver observer);

    BlockRegistry getRegistry();

    CratePositionRegistry getPositionRegistry();
}
