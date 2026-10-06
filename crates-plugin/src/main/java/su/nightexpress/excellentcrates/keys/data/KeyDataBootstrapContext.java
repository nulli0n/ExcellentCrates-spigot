package su.nightexpress.excellentcrates.keys.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.data.extension.KeyDataExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.data.codec.KeyBaseCodec;
import su.nightexpress.excellentcrates.keys.data.codec.KeyDisplayCodec;
import su.nightexpress.excellentcrates.keys.data.codec.KeyItemCodec;
import su.nightexpress.excellentcrates.keys.data.controller.KeyDataLoadController;
import su.nightexpress.excellentcrates.keys.data.controller.KeyDataSaveController;
import su.nightexpress.excellentcrates.keys.data.io.KeyIOService;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyBase;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyDisplay;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyItem;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class KeyDataBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("keys.data");
    private static final String     BUNDLE_NAME = "IO/Data";

    private static final String DIR_KEYS = "key";

    public final TinyRegistry<KeyDataExtension> extensions;

    public final KeyIOService   ioService;
    public final KeyDataService dataService;

    public KeyDataBootstrapContext(CratesPlugin plugin, KeyRegistry registry,
                                   ReadOnlySettings<KeyCoreSettings> settings) {
        super(BUNDLE_ID, BUNDLE_NAME);
        this.registerCodecs();
        this.extensions = new SimpleRegistry<>();

        Path keysDir = plugin.objectsPath().resolve(DIR_KEYS);

        this.ioService = new KeyIOService(keysDir, this.extensions);
        this.dataService = new KeyDataService(this.ioService, registry, this.extensions);

        this.addComponent(new KeyDataLoadController(registry, dataService));
        this.addComponent(new KeyDataSaveController(plugin, this.dataService, settings));
    }

    private void registerCodecs() {
        ConfigCodecs.register(StandardKeyBase.class, KeyBaseCodec.INSTANCE);
        ConfigCodecs.register(StandardKeyDisplay.class, KeyDisplayCodec.INSTANCE);
        ConfigCodecs.register(StandardKeyItem.class, KeyItemCodec.INSTANCE);
    }
}
