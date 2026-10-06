package su.nightexpress.excellentcrates.crates.hologram.provider;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramTextProvider;

@NullMarked
public class NoOpHologramProvider implements HologramProvider {

    public static final NoOpHologramProvider INSTANCE = new NoOpHologramProvider();

    private static final Identifier ID = new Identifier("noop");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void removeAll() {
        // No-op
    }

    @Override
    public void render(Crate crate, CratePosition position, Location location, World world,
                       HologramTextProvider textProvider) {
        // No-op
    }

    @Override
    public void remove(Crate crate) {
        // No-op
    }

    @Override
    public void disableFor(Crate crate, Player player) {
        // No-op
    }

    @Override
    public void enableFor(Crate crate, Player player) {
        // No-op
    }
}
