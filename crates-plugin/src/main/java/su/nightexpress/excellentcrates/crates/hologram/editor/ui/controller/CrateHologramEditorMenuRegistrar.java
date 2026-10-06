package su.nightexpress.excellentcrates.crates.hologram.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIKeys;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.CrateHologramOptionsMenu;

@NullMarked
public class CrateHologramEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                    plugin;
    private final CoreUIService                   coreUI;
    private final CrateRegistry                   crateRegistry;
    private final CrateHologramEditorUIController uiController;


    public CrateHologramEditorMenuRegistrar(CratesPlugin plugin,
                                            CoreUIService coreUI,
                                            CrateRegistry crateRegistry,
                                            CrateHologramEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateRegistry = crateRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        CrateHologramOptionsMenu optionsMenu = new CrateHologramOptionsMenu(plugin, crateRegistry, uiController);

        optionsMenu.load();

        this.coreUI.registerMenu(CrateHologramEditorUIKeys.MENU_OPTIONS, optionsMenu);
    }
}
