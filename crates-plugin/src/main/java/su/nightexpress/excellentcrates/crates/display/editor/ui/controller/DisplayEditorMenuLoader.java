package su.nightexpress.excellentcrates.crates.display.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.display.editor.ui.CrateDisplayEditorUIKeys;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.menu.CrateDisplaySettingsMenu;

@NullMarked
public class DisplayEditorMenuLoader implements StartupComponent {

    private final CratesPlugin              plugin;
    private final CoreUIService             coreUI;
    private final CrateRegistry             crateRegistry;
    private final DisplayEditorUIController uiController;

    public DisplayEditorMenuLoader(CratesPlugin plugin,
                                   CoreUIService coreUI,
                                   CrateRegistry crateRegistry,
                                   DisplayEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateRegistry = crateRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        CrateDisplaySettingsMenu settingsMenu = new CrateDisplaySettingsMenu(plugin, crateRegistry, uiController);

        settingsMenu.load();

        this.coreUI.registerMenu(CrateDisplayEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
