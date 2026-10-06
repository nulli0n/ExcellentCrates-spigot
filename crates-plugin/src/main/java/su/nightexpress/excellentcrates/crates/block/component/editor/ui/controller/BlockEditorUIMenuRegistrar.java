package su.nightexpress.excellentcrates.crates.block.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIKeys;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.BlockCatalogMenu;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.BlockSettingsMenu;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;

@NullMarked
public class BlockEditorUIMenuRegistrar implements StartupComponent {

    private final CratesPlugin            plugin;
    private final CoreUIService           coreUI;
    private final CrateResolver           crateResolver;
    private final BlockRegistry           blockRegistry;
    private final BlockItemService        itemService;
    private final BlockEditorUIController controller;

    public BlockEditorUIMenuRegistrar(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      CrateResolver crateResolver,
                                      BlockRegistry blockRegistry,
                                      BlockItemService itemService,
                                      BlockEditorUIController controller
    ) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateResolver = crateResolver;
        this.blockRegistry = blockRegistry;
        this.itemService = itemService;
        this.controller = controller;
    }

    @Override
    public void start() {
        BlockSettingsMenu componentMenu = new BlockSettingsMenu(plugin, crateResolver, controller);
        BlockCatalogMenu catalogMenu = new BlockCatalogMenu(plugin, crateResolver, blockRegistry, itemService,
            controller);

        componentMenu.load();
        catalogMenu.load();

        this.coreUI.registerMenu(BlockEditorUIKeys.MENU_CATALOG, catalogMenu);
        this.coreUI.registerMenu(BlockEditorUIKeys.MENU_COMPONENT, componentMenu);
    }
}
