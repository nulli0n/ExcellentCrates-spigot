package su.nightexpress.excellentcrates.crates.hologram;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramsAPI;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.hologram.block.HologramPositionObserver;
import su.nightexpress.excellentcrates.crates.hologram.component.StandardHologramComponent;
import su.nightexpress.excellentcrates.crates.hologram.component.codec.HologramComponentCodec;
import su.nightexpress.excellentcrates.crates.hologram.component.codec.HologramOffsetCodec;
import su.nightexpress.excellentcrates.crates.hologram.component.data.StandardHologramOffset;
import su.nightexpress.excellentcrates.crates.hologram.component.extension.HologramComponentExtension;
import su.nightexpress.excellentcrates.crates.hologram.controller.HologramProviderLifecycleController;
import su.nightexpress.excellentcrates.crates.hologram.controller.HologramUpdateController;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
import su.nightexpress.excellentcrates.crates.hologram.pipeline.HologramPipelineExecutor;
import su.nightexpress.excellentcrates.crates.hologram.pipeline.HologramPipelineStage;
import su.nightexpress.excellentcrates.crates.hologram.provider.ActiveHologramProvider;
import su.nightexpress.excellentcrates.crates.hologram.settings.HologramSettings;
import su.nightexpress.excellentcrates.integration.packetevents.PacketEventsHologramProviderConfiguration;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;

@NullMarked
public final class HologramBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.holograms");
    private static final String     BUNDLE_NAME = "Holograms";

    private static final String SETTINGS_FILE_NAME = "crates.holograms.yml";

    public final HologramDisplayService displayService;

    private final HologramComponentExtension dataExtension;
    private final HologramPipelineStage      pipelineStage;
    private final HologramPipelineExecutor   pipelineExecutor;

    public final HologramsAPI api;

    public HologramBootstrapContext(CratesPlugin plugin,
                                    CrateRegistry crateRegistry,
                                    CratePlaceholders cratePlaceholders,
                                    CratePositionRegistry blockResolver) {
        super(BUNDLE_ID, BUNDLE_NAME);

        plugin.injectLang(HologramsLang.class);

        ConfigCodecs.register(StandardHologramOffset.class, HologramOffsetCodec.INSTANCE);
        ConfigCodecs.register(StandardHologramComponent.class, HologramComponentCodec.INSTANCE);

        IdentifiableRegistry<HologramProvider> providers = new IdentifiableRegistry<>();

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<HologramSettings> settings = new SettingsProvider<HologramSettings>(HologramSettings.DEFAULT);

        ActiveHologramProvider proxy = new ActiveHologramProvider();

        this.displayService = new HologramDisplayService(proxy, crateRegistry, blockResolver, cratePlaceholders);

        blockResolver.addObserver(new HologramPositionObserver(crateRegistry, displayService));

        this.dataExtension = new HologramComponentExtension(displayService);
        this.pipelineStage = new HologramPipelineStage(proxy);
        this.pipelineExecutor = new HologramPipelineExecutor(proxy);

        this.api = new DefaultHologramsAPI(providers, displayService);

        this.addComponent(new SettingsController<>(settingsPath, HologramSettings::loadFrom, settings));
        this.addComponent(new HologramProviderLifecycleController(proxy, providers, settings, displayService));
        this.addComponent(new HologramUpdateController(plugin, displayService, settings));

        this.configureProviders(plugin);
    }

    private void configureProviders(CratesPlugin plugin) {
        if (Plugins.isInstalled("packetevents") && Version.isPaper()) {
            this.addComponent(PacketEventsHologramProviderConfiguration.configure(plugin, this.api));
        }
    }

    public HologramComponentExtension getDataExtension() {
        return this.dataExtension;
    }

    public HologramPipelineStage getPipelineStage() {
        return this.pipelineStage;
    }

    public HologramPipelineExecutor getPipelineExecutor() {
        return this.pipelineExecutor;
    }
}
