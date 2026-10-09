package su.nightexpress.excellentcrates.crates.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.data.CrateDataAPI;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.config.settings.CrateCoreSettings;
import su.nightexpress.excellentcrates.crates.data.codec.CrateBaseCodec;
import su.nightexpress.excellentcrates.crates.data.codec.CrateDisplayCodec;
import su.nightexpress.excellentcrates.crates.data.codec.CrateItemCodec;
import su.nightexpress.excellentcrates.crates.data.controller.CrateDataLoadController;
import su.nightexpress.excellentcrates.crates.data.controller.CrateDataSaveController;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateBase;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateDisplay;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateItem;
import su.nightexpress.excellentcrates.crates.data.io.CrateIOService;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class CrateDataContext extends NamedBootstrapContext {

    private static final String DIR_CRATES = "crate";

    private static final Identifier BUNDLE_ID   = new Identifier("crates.data");
    private static final String     BUNDLE_NAME = "IO/Data";

    public final TinyRegistry<CrateDataExtension> extensions;

    public final CrateIOService   ioService;
    public final CrateDataService dataService;
    public final CrateDataAPI     api;

    public CrateDataContext(CratesPlugin plugin, CrateRegistry registry, ReadOnlySettings<CrateCoreSettings> settings) {
        super(BUNDLE_ID, BUNDLE_NAME);
        this.extensions = new SimpleRegistry<>();
        this.registerCodecs();

        Path cratesDir = plugin.objectsPath().resolve(DIR_CRATES);

        this.ioService = new CrateIOService(cratesDir, this.extensions);
        this.dataService = new CrateDataService(this.ioService, registry, this.extensions);

        this.api = new DefaultCrateDataAPI(this.extensions, this.dataService);

        this.addComponent(new CrateDataSaveController(plugin, this.dataService, settings));
        this.addComponent(new CrateDataLoadController(registry, this.dataService));
    }

    private void registerCodecs() {
        ConfigCodecs.register(StandardCrateBase.class, CrateBaseCodec.INSTANCE);
        ConfigCodecs.register(StandardCrateDisplay.class, CrateDisplayCodec.INSTANCE);
        ConfigCodecs.register(StandardCrateItem.class, CrateItemCodec.INSTANCE);
    }
}
