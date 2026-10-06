package su.nightexpress.excellentcrates.crates.cooldown;

import java.nio.file.Path;
import java.time.Duration;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownsAPI;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholder;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.quota.CrateQuotaProcessor;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.cooldown.component.StandardCrateCooldownComponent;
import su.nightexpress.excellentcrates.crates.cooldown.component.codec.CrateCooldownsCodec;
import su.nightexpress.excellentcrates.crates.cooldown.component.extension.CrateCooldownsLifecycleExtension;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownCachedDataService;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownDBSettings;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownSQLRepository;
import su.nightexpress.excellentcrates.crates.cooldown.db.controller.CrateCooldownDatabaseInitializer;
import su.nightexpress.excellentcrates.crates.cooldown.db.controller.CrateCooldownPlayerSessionController;
import su.nightexpress.excellentcrates.crates.cooldown.db.controller.CrateCooldownSaveController;
import su.nightexpress.excellentcrates.crates.cooldown.editor.CrateCooldownsEditorBootstrapContext;
import su.nightexpress.excellentcrates.crates.cooldown.lang.CrateCooldownsLang;
import su.nightexpress.excellentcrates.crates.cooldown.placeholder.CrateCooldownPlaceholder;
import su.nightexpress.excellentcrates.crates.cooldown.quota.CrateCooldownQuotaProcessor;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class CrateCooldownBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.cooldown");
    private static final String     BUNDLE_NAME = "Cooldowns";

    private static final String SETTINGS_FILE = "crates.cooldowns.yml";

    public final CrateCooldownService cooldownService;
    public final CrateCooldownsAPI    api;

    private final CrateCooldownsEditorBootstrapContext editorContext;

    private final CrateDataExtension  dataExtension;
    private final CrateQuotaProcessor quotaProcessor;
    private final CratePlaceholder    cratePlaceholder;

    public CrateCooldownBootstrapContext(CratesPlugin plugin,
                                         DatabaseClient databaseClient,
                                         CoreUIService coreUI,
                                         MessageDispatcher dispatcher,
                                         CrateResolver crateResolver,
                                         CratePlaceholders cratePlaceholders) {
        super(BUNDLE_ID, BUNDLE_NAME);
        plugin.injectLang(CrateCooldownsLang.class);

        ConfigCodecs.register(StandardCrateCooldownComponent.class, CrateCooldownsCodec.INSTANCE);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE);
        CrateCooldownDBSettings dbSettings = CrateCooldownDBSettings.loadFrom(settingsPath);

        Duration cacheTTL = Duration.ofMinutes(dbSettings.cacheTTL());

        CrateCooldownSQLRepository repository = new CrateCooldownSQLRepository(databaseClient, dbSettings);
        CrateCooldownCachedDataService cacheService = new CrateCooldownCachedDataService(repository, cacheTTL);
        this.cooldownService = new CrateCooldownService(cacheService);

        this.editorContext = new CrateCooldownsEditorBootstrapContext(
            plugin, coreUI, dispatcher, crateResolver, cratePlaceholders
        );

        this.api = new DefaultCrateCooldownsAPI(this.cooldownService);

        this.dataExtension = new CrateCooldownsLifecycleExtension(cacheService);
        this.quotaProcessor = new CrateCooldownQuotaProcessor(this.cooldownService);
        this.cratePlaceholder = new CrateCooldownPlaceholder(cooldownService);

        this.addComponent(new CrateCooldownDatabaseInitializer(repository, cacheService, this.cooldownService));
        this.addComponent(new CrateCooldownSaveController(plugin, cacheService, dbSettings.dataSaveInterval()));
        this.addComponent(new CrateCooldownPlayerSessionController(plugin, cacheService));
        this.addComponent(this.editorContext);
    }

    public CrateDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public CrateEditorExtension getEditorExtension() {
        return this.editorContext.editorExtension;
    }

    public CrateQuotaProcessor getQuotaProcessor() {
        return this.quotaProcessor;
    }

    public CratePlaceholder getCratePlaceholder() {
        return this.cratePlaceholder;
    }
}
