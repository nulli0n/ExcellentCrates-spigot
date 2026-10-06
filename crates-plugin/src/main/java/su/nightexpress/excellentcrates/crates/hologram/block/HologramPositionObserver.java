package su.nightexpress.excellentcrates.crates.hologram.block;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionObserver;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;

@NullMarked
public class HologramPositionObserver implements CratePositionObserver {

    private final CrateRegistry          crateRegistry;
    private final HologramDisplayService displayService;

    public HologramPositionObserver(CrateRegistry crateRegistry, HologramDisplayService displayService) {
        this.crateRegistry = crateRegistry;
        this.displayService = displayService;
    }

    @Override
    public void onPositionAdded(Identifier crateId, CratePosition position) {
        Crate crate = this.crateRegistry.get(crateId);
        if (crate == null) return;

        this.displayService.update(crate);
    }

    @Override
    public void onPositionRemoved(Identifier crateId, CratePosition position) {
        Crate crate = this.crateRegistry.get(crateId);
        if (crate == null) return;

        this.displayService.removeAll(crate);
        this.displayService.update(crate); // Update the crate to re-render holograms for remaining positions
    }
}
