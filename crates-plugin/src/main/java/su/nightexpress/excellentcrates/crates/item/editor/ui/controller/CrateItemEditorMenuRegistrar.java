package su.nightexpress.excellentcrates.crates.item.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIKeys;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIController;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.CrateItemSettingsMenu;

@NullMarked
public class CrateItemEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                plugin;
    private final CoreUIService               coreUI;
    private final CrateRegistry               crateRegistry;
    private final ICrateItemFactory           crateRenderer;
    private final CrateItemEditorUIController uiController;

    public CrateItemEditorMenuRegistrar(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        CrateRegistry crateRegistry,
                                        ICrateItemFactory crateRenderer,
                                        CrateItemEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateRegistry = crateRegistry;
        this.crateRenderer = crateRenderer;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        CrateItemSettingsMenu settingsMenu = new CrateItemSettingsMenu(
            plugin, crateRegistry, crateRenderer, uiController
        );

        settingsMenu.load();

        this.coreUI.registerMenu(CrateItemEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
