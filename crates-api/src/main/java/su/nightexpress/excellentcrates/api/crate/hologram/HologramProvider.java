package su.nightexpress.excellentcrates.api.crate.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;

@NullMarked
public interface HologramProvider extends Identifiable {

    void removeAll();

    void render(Crate crate, CratePosition position, Location location, World world, HologramTextProvider textProvider);

    void remove(Crate crate);

    void disableFor(Crate crate, Player player);

    void enableFor(Crate crate, Player player);
}
