package su.nightexpress.excellentcrates.crates.block.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIKeys;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.BlockSettingsMenu;

@NullMarked
public class BlockEditorUIMenuRegistrar implements StartupComponent {

    private final CratesPlugin            plugin;
    private final CoreUIService           coreUI;
    private final CrateResolver           crateResolver;
    private final BlockEditorUIController controller;

    public BlockEditorUIMenuRegistrar(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      CrateResolver crateResolver,
                                      BlockEditorUIController controller
    ) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateResolver = crateResolver;
        this.controller = controller;
    }

    @Override
    public void start() {
        BlockSettingsMenu componentMenu = new BlockSettingsMenu(plugin, crateResolver, controller);

        componentMenu.load();

        this.coreUI.registerMenu(BlockEditorUIKeys.MENU_COMPONENT, componentMenu);
    }
}
