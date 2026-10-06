package su.nightexpress.excellentcrates.crates.hologram.controller;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;
import su.nightexpress.excellentcrates.crates.hologram.provider.ActiveHologramProvider;
import su.nightexpress.excellentcrates.crates.hologram.provider.NoOpHologramProvider;
import su.nightexpress.excellentcrates.crates.hologram.settings.HologramSettings;

@NullMarked
public class HologramProviderLifecycleController extends BasePluginComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HologramProviderLifecycleController.class);

    private final ActiveHologramProvider                 proxy;
    private final IdentifiableRegistry<HologramProvider> providers;
    private final ReadOnlySettings<HologramSettings>     settings;
    private final HologramDisplayService                 displayService;

    public HologramProviderLifecycleController(ActiveHologramProvider proxy,
                                               IdentifiableRegistry<HologramProvider> providers,
                                               ReadOnlySettings<HologramSettings> settings,
                                               HologramDisplayService displayService) {
        super();
        this.proxy = proxy;
        this.providers = providers;
        this.settings = settings;
        this.displayService = displayService;
    }

    @Override
    protected void onReload() {
        Identifier configuredId = this.settings.get().providerId();
        HologramProvider activeProvider = this.providers.get(configuredId);

        if (activeProvider != null) {
            this.proxy.setDelegate(activeProvider);
        }
        else {
            this.proxy.setDelegate(NoOpHologramProvider.INSTANCE);
            LOGGER.warn("Hologram provider not found: {}. Using NoOpHologramProvider.", configuredId);
        }

        // Force a re-render of all holograms using the newly swapped provider
        this.displayService.updateHolograms();
    }

    @Override
    protected void onShutdown() {
        this.proxy.removeAll();
    }

    @Override
    protected void onStart() {
        this.reload();
    }
}
