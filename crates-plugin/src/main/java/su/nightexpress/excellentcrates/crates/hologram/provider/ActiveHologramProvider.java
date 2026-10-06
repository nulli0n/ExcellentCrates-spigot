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
public class ActiveHologramProvider implements HologramProvider {

    private volatile HologramProvider delegate = NoOpHologramProvider.INSTANCE;

    public void setDelegate(HologramProvider delegate) {
        this.delegate.removeAll();
        this.delegate = delegate;
    }

    @Override
    public void removeAll() {
        this.delegate.removeAll();
    }

    @Override
    public void render(Crate crate, CratePosition position, Location location, World world,
                       HologramTextProvider textProvider) {
        this.delegate.render(crate, position, location, world, textProvider);
    }

    @Override
    public void remove(Crate crate) {
        this.delegate.remove(crate);
    }

    @Override
    public Identifier getId() {
        return this.delegate.getId();
    }

    @Override
    public void disableFor(Crate crate, Player player) {
        this.delegate.disableFor(crate, player);
    }

    @Override
    public void enableFor(Crate crate, Player player) {
        this.delegate.enableFor(crate, player);
    }
}
