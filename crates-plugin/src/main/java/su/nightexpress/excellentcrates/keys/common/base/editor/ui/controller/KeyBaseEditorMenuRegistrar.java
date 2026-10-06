package su.nightexpress.excellentcrates.keys.common.base.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIController;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIKeys;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu.KeyBaseMainMenu;

@NullMarked
public class KeyBaseEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin              plugin;
    private final CoreUIService             coreUI;
    private final KeyRegistry               registry;
    private final KeyBaseEditorUIController controller;

    public KeyBaseEditorMenuRegistrar(CratesPlugin plugin, CoreUIService coreUI, KeyRegistry registry,
                                      KeyBaseEditorUIController controller) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.controller = controller;
    }

    @Override
    public void start() {
        KeyBaseMainMenu mainMenu = new KeyBaseMainMenu(plugin, registry, controller);

        mainMenu.load();

        this.coreUI.registerMenu(KeyBaseEditorUIKeys.MAIN_MENU, mainMenu);
    }

}
