package su.nightexpress.excellentcrates.crates.block;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.settings.FastSettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.block.addon.BlockAddonConfiguration;
import su.nightexpress.excellentcrates.crates.block.command.argument.BlockArgumentType;
import su.nightexpress.excellentcrates.crates.block.command.tree.GetBlockCommand;
import su.nightexpress.excellentcrates.crates.block.controller.CratePositionWorldController;
import su.nightexpress.excellentcrates.crates.block.handler.BlockInteractionHandler;
import su.nightexpress.excellentcrates.crates.block.handler.BlockPlacementHandler;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.excellentcrates.crates.block.permission.BlocksPerms;
import su.nightexpress.excellentcrates.crates.block.position.DefaultCratePositionRegistry;
import su.nightexpress.excellentcrates.crates.block.settings.BlockSettings;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;
import su.nightexpress.excellentcrates.crates.interact.CrateInteractionService;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.commands.Arguments;

@NullMarked
public final class BlockCoreBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.blocks.core");
    private static final String     BUNDLE_NAME = "Blocks";

    private static final String SETTINGS_FILE = "crates.blocks.yml";

    private final List<CrateCommand> crateCommands;

    public final BlockRegistry                registry;
    public final DefaultCratePositionRegistry positions;

    public final BlockItemService itemService;

    public final BlockPlacementHandler   placementHandler;
    public final BlockInteractionHandler interactionHandler;

    public final BlockAPI api;

    public BlockCoreBootstrapContext(CratesPlugin plugin,
                                     CrateMessageDispatcher dispatcher,
                                     CrateRegistry crateRegistry,
                                     CratePlaceholders cratePlaceholders,
                                     CrateDataService dataService,
                                     CrateInteractionService interactionService) {
        super(BUNDLE_ID, BUNDLE_NAME);

        this.registry = new BlockRepository();
        this.crateCommands = new ArrayList<>();

        Arguments.register(CrateBlock.class, new BlockArgumentType(this.registry));

        plugin.injectLang(BlocksLang.class);
        plugin.registerPermissions(BlocksPerms.ROOT);

        KeyDomain keyDomain = plugin.keyDomain();
        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE);
        SettingsProvider<BlockSettings> settings = new SettingsProvider<>(BlockSettings.defaults());
        this.addComponent(new FastSettingsController<>(settingsPath, BlockSettings::loadFrom, settings));

        this.positions = new DefaultCratePositionRegistry(crateRegistry);

        BlockLinkService linkService = new BlockLinkService(dataService, positions);
        this.itemService = new BlockItemService(settings, cratePlaceholders, registry, crateRegistry, keyDomain);
        BlockResolveService resolveService = new BlockResolveService(registry);

        this.placementHandler = new BlockPlacementHandler(itemService, linkService, positions, dispatcher);
        this.interactionHandler = new BlockInteractionHandler(
            settings, resolveService, positions, interactionService, dispatcher
        );

        this.addComponent(new CratePositionWorldController(plugin, crateRegistry, positions));

        this.crateCommands.add(new GetBlockCommand(this.itemService, dispatcher));

        this.api = new DefaultBlockAPI(registry, positions, placementHandler, interactionHandler);

        this.addComponent(BlockAddonConfiguration.configure(plugin, this.api, settings));
    }

    public List<CrateCommand> getCommands() {
        return Collections.unmodifiableList(this.crateCommands);
    }
}
