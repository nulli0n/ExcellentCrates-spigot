package su.nightexpress.excellentcrates.crates.hologram;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramsAPI;

@NullMarked
public class DefaultHologramsAPI implements HologramsAPI {

    private final IdentifiableRegistry<HologramProvider> providers;
    private final HologramDisplayService                 displayService;

    public DefaultHologramsAPI(IdentifiableRegistry<HologramProvider> providers,
                               HologramDisplayService displayService) {
        this.providers = providers;
        this.displayService = displayService;
    }

    @Override
    public void registerProvider(HologramProvider provider) {
        this.providers.register(provider);
    }

    @Override
    public void removeAll() {
        this.displayService.removeAll();
    }

    @Override
    public void removeAll(Crate crate) {
        this.displayService.removeAll(crate);
    }

    @Override
    public void updateHolograms() {
        this.displayService.updateHolograms();
    }

    @Override
    public void update(Crate crate) {
        this.displayService.update(crate);
    }
}
