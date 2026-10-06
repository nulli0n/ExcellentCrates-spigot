package su.nightexpress.excellentcrates.keys.display.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIKeys;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.menu.KeyDisplayMainMenu;

@NullMarked
public class KeyDisplayEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                 plugin;
    private final CoreUIService                coreUI;
    private final KeyRegistry                  registry;
    private final KeyDisplayEditorUIController controller;

    public KeyDisplayEditorMenuRegistrar(CratesPlugin plugin,
                                         CoreUIService coreUI,
                                         KeyRegistry registry,
                                         KeyDisplayEditorUIController controller) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.controller = controller;
    }

    @Override
    public void start() {
        KeyDisplayMainMenu mainMenu = new KeyDisplayMainMenu(plugin, registry, controller);

        mainMenu.load();

        this.coreUI.registerMenu(KeyDisplayEditorUIKeys.MENU_MAIN, mainMenu);
    }

}
