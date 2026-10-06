package su.nightexpress.excellentcrates.api.crate.interact.context;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public record LocatedCrateInteractContext(Player player,
                                          Crate crate,
                                          Location location) implements CrateInteractContext {

}
