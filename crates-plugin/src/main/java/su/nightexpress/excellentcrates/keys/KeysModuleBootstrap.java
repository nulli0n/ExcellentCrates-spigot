package su.nightexpress.excellentcrates.keys;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.api.DefaultKeysAPI;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceBootstrapContext;
import su.nightexpress.excellentcrates.keys.command.KeyCommandBootstrapContext;
import su.nightexpress.excellentcrates.keys.common.base.KeyBaseBootstrapContext;
import su.nightexpress.excellentcrates.keys.config.KeyConfigBootstrapContext;
import su.nightexpress.excellentcrates.keys.cost.KeyCostBootstrapContext;
import su.nightexpress.excellentcrates.keys.cost.component.KeyCostComponentContext;
import su.nightexpress.excellentcrates.keys.cost.editor.KeyCostEditorBootstrapContext;
import su.nightexpress.excellentcrates.keys.data.KeyDataBootstrapContext;
import su.nightexpress.excellentcrates.keys.dispatcher.DefaultKeyMessageDispatcher;
import su.nightexpress.excellentcrates.keys.display.KeyDisplayBootstrapContext;
import su.nightexpress.excellentcrates.keys.editor.KeyEditorBootstrapContext;
import su.nightexpress.excellentcrates.keys.item.KeyItemBootstrapContext;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.excellentcrates.keys.placeholder.KeyPlaceholderAPIResolver;
import su.nightexpress.excellentcrates.keys.placeholder.KeyPlaceholderService;
import su.nightexpress.excellentcrates.keys.storage.KeyStorageBootstrapContext;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public final class KeysModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        DatabaseClient databaseClient = dependencies.databaseClient();
        UserDataManager userDataService = dependencies.userService();

        CrateRegistry crates = dependencies.crateRegistry();
        KeyRegistry keys = new KeyRepository();
        KeyPlaceholders placeholders = new KeyPlaceholderService();
        KeyMessageDispatcher dispatcher = new DefaultKeyMessageDispatcher(dependencies.dispatcher(), placeholders);

        plugin.injectLang(KeyLang.class);
        plugin.registerPermissions(KeyPerms.ROOT);
        plugin.addPlaceholderAPIResolver(new KeyPlaceholderAPIResolver(keys, placeholders));

        // ================================
        // Core Services Bootstrap
        // ================================

        KeyConfigBootstrapContext configContext = new KeyConfigBootstrapContext(plugin);
        KeyCommandBootstrapContext commandContext = new KeyCommandBootstrapContext(
            plugin, keys, configContext.settings
        );
        KeyDataBootstrapContext dataContext = new KeyDataBootstrapContext(plugin, keys, configContext.settings);
        KeyBaseBootstrapContext baseContext = new KeyBaseBootstrapContext(plugin, dispatcher, coreUI, keys);

        KeyDisplayBootstrapContext displayContext = new KeyDisplayBootstrapContext(
            plugin, coreUI, dispatcher, keys, configContext.settings
        );

        KeyItemBootstrapContext itemContext = new KeyItemBootstrapContext(
            plugin, coreUI, dispatcher, keys, placeholders
        );

        KeyEditorBootstrapContext editorContext = new KeyEditorBootstrapContext(
            plugin, coreUI, dispatcher, keys, itemContext.itemFactory, dataContext.dataService
        );

        KeyStorageBootstrapContext storageContext = new KeyStorageBootstrapContext(
            plugin, databaseClient, configContext.settings
        );

        KeyBalanceBootstrapContext balanceContext = new KeyBalanceBootstrapContext(
            plugin, dispatcher, userDataService, keys, storageContext.storageService, itemContext.itemService
        );

        this.registerComponent(configContext);
        this.registerComponent(commandContext);
        this.registerComponent(dataContext);
        this.registerComponent(baseContext);
        this.registerComponent(displayContext);
        this.registerComponent(itemContext);
        this.registerComponent(editorContext);
        this.registerComponent(storageContext);
        this.registerComponent(balanceContext);

        placeholders.registerPlaceholder(displayContext.getPlaceholder());
        placeholders.registerPlaceholder(balanceContext.getPlaceholder());

        editorContext.extensions.register(baseContext.getEditorExtension());
        editorContext.extensions.register(displayContext.getEditorExtension());
        editorContext.extensions.register(itemContext.getEditorExtension());

        commandContext.commands.register(editorContext.getEditorCommand());
        commandContext.commands.registerAll(itemContext.getCommands());
        commandContext.commands.registerAll(balanceContext.getCommands());

        // ================================
        // Key Cost Bootstrap
        // ================================

        KeyCostBootstrapContext costContext = new KeyCostBootstrapContext(
            plugin, keys, displayContext.resolver, itemContext.itemFactory, balanceContext.balanceService
        );

        KeyCostEditorBootstrapContext costEditorContext = new KeyCostEditorBootstrapContext(
            plugin, coreUI, dispatcher, crates, placeholders, keys, itemContext.itemFactory
        );

        KeyCostComponentContext costComponentContext = new KeyCostComponentContext();

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.editor().registerExtension(costEditorContext.getEditorExtension());
            cratesApi.data().registerExtension(costComponentContext.getDataExtension());
        });

        this.bridge.onAvailable(CoreServices.COST, costApi -> {
            costApi.registerType(costContext.getCostType());
        });

        this.registerComponent(costContext);
        this.registerComponent(costEditorContext);
        this.registerComponent(costComponentContext);

        KeysAPI api = new DefaultKeysAPI(balanceContext.api, itemContext.api);

        services.register(CoreServices.KEYS, api);
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new KeysModule();
    }
}
