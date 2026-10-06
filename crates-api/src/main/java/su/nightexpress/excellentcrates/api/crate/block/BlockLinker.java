package su.nightexpress.excellentcrates.api.crate.block;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface BlockLinker {

    ActionResult linkCrateBlock(Crate crate, Location location);

    ActionResult unlinkCrateBlock(Crate crate, Location location);
}
