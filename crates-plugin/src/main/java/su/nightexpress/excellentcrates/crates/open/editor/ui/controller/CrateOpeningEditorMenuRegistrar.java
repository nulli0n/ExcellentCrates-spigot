package su.nightexpress.excellentcrates.crates.open.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIKeys;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIController;
import su.nightexpress.excellentcrates.crates.open.editor.ui.menu.CrateOpeningSettingsMenu;

@NullMarked
public class CrateOpeningEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                   plugin;
    private final CoreUIService                  coreUI;
    private final CrateResolver                  crateResolver;
    private final CrateOpeningEditorUIController uiController;

    public CrateOpeningEditorMenuRegistrar(CratesPlugin plugin,
                                           CoreUIService coreUI,
                                           CrateResolver crateResolver,
                                           CrateOpeningEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateResolver = crateResolver;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        CrateOpeningSettingsMenu settingsMenu = new CrateOpeningSettingsMenu(plugin, crateResolver, uiController);

        settingsMenu.load();

        this.coreUI.registerMenu(CrateOpeningEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
