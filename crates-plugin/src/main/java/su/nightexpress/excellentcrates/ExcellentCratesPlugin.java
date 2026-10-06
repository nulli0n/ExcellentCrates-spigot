package su.nightexpress.excellentcrates;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.engine.ui.menu.MenuRegistry;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.PluginAPIConfiguration;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.SharedDirectories;
import su.nightexpress.excellentcrates.core.crate.CratePlaceholderService;
import su.nightexpress.excellentcrates.core.dispatcher.StandardMessageDispatcher;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.excellentcrates.core.ui.DefaultCoreUIService;
import su.nightexpress.excellentcrates.core.ui.DefaultMenuRegistry;
import su.nightexpress.excellentcrates.crates.registry.DefaultCrateRegistry;
import su.nightexpress.excellentcrates.engine.CodecRegistrar;
import su.nightexpress.excellentcrates.engine.command.PluginCommandRegistrar;
import su.nightexpress.excellentcrates.engine.config.PluginConfigBootstrapContext;
import su.nightexpress.excellentcrates.engine.database.DatabaseBootstrapContext;
import su.nightexpress.excellentcrates.engine.dispatcher.DefaultCrateMessageDispatcher;
import su.nightexpress.excellentcrates.engine.module.ModuleBootstrapContext;
import su.nightexpress.excellentcrates.integration.papi.PlaceholderAPIConfiguration;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.integration.placeholder.PAPI;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public class ExcellentCratesPlugin extends NightPlugin implements CratesPlugin {

    private final ComponentBundle              core          = new ComponentBundle();
    private final List<PlaceholderAPIResolver> papiResolvers = new ArrayList<>();

    @Nullable
    private KeyDomain keyDomain;

    @Override
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("see-config-folder", new String[]{"see-config-folder"});
    }

    @Override
    protected boolean disableCommandManager() {
        return true;
    }

    @Override
    protected void addRegistries() {
        this.registerLang(Lang.class);
    }

    @Override
    protected void registerCommands() {

    }

    @Override
    public void enable() {
        this.keyDomain = KeyDomain.of(this);

        // Register core codecs
        CodecRegistrar.register();

        // Create core registries
        ServiceRegistry services = new ServiceRegistry();
        MenuRegistry menuRegistry = new DefaultMenuRegistry();

        // Create bootstrap contexts for configuration, database, and modules
        PluginConfigBootstrapContext configContext = new PluginConfigBootstrapContext(this);
        DatabaseBootstrapContext dbContext = new DatabaseBootstrapContext(this);
        ModuleBootstrapContext moduleContext = new ModuleBootstrapContext(this);

        // Create core services and dependencies
        MessageDispatcher dispatcher = new StandardMessageDispatcher(() -> configContext.settings.get().prefix());
        CoreUIService uiService = new DefaultCoreUIService(menuRegistry, this.dialogRegistry);
        UserDataManager userDataService = this.getUserDataManager();

        CrateRegistry crateRegistry = new DefaultCrateRegistry();
        CratePlaceholderService cratePlaceholders = new CratePlaceholderService();
        CrateMessageDispatcher crateDispatcher = new DefaultCrateMessageDispatcher(dispatcher, cratePlaceholders);

        DefaultDependencies dependencies = new DefaultDependencies(
            this,
            uiService,
            userDataService,
            dbContext.databaseClient,
            crateRegistry,
            cratePlaceholders,
            crateDispatcher
        );

        this.core.addComponent(configContext.getComponent());
        this.core.addComponent(new PluginCommandRegistrar(this, this.core, configContext.settings, dispatcher));

        moduleContext.getBootstraps().forEach(bootstrap -> {
            bootstrap.onRegister(services, dependencies);
        });

        moduleContext.getBootstraps().forEach(bootstrap -> {
            this.core.addComponent(bootstrap.onResolve(services, dependencies));
        });

        if (PAPI.isPresent()) {
            this.core.addComponent(PlaceholderAPIConfiguration.configure(this, this.papiResolvers));
        }

        this.core.start();

        this.registerPermissions(Perms.ROOT);

        PluginAPIConfiguration.configure(this, services);
    }

    @Override
    public void addPlaceholderAPIResolver(PlaceholderAPIResolver resolver) {
        this.papiResolvers.add(resolver);
    }

    @Override
    public void disable() {
        this.core.shutdown();
    }

    @Override
    public void reload() {
        this.langRegistry.loadLocale();
        this.core.reload();
    }

    @Override
    public KeyDomain keyDomain() {
        return Objects.requireNonNull(this.keyDomain);
    }

    @Override
    public Path configPath() {
        return this.dataPath().resolve(SharedDirectories.DIR_CONFIG);
    }

    @Override
    public Path objectsPath() {
        return this.dataPath().resolve(SharedDirectories.DIR_OBJECTS);
    }

    @Override
    public Path menuPath() {
        return this.dataPath().resolve(SharedDirectories.DIR_MENU);
    }

    @Override
    public Path logsPath() {
        return this.dataPath().resolve(SharedDirectories.DIR_LOGS);
    }
}
