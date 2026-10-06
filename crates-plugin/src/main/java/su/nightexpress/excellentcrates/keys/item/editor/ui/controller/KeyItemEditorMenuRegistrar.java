package su.nightexpress.excellentcrates.keys.item.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.excellentcrates.keys.item.editor.KeyItemEditorUIKeys;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIController;
import su.nightexpress.excellentcrates.keys.item.editor.ui.menu.KeyItemMainMenu;

@NullMarked
public class KeyItemEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin              plugin;
    private final CoreUIService             coreUI;
    private final KeyRegistry               registry;
    private final KeyItemFactory            itemFactory;
    private final KeyItemEditorUIController uiController;

    public KeyItemEditorMenuRegistrar(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      KeyRegistry registry,
                                      KeyItemFactory itemFactory,
                                      KeyItemEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.itemFactory = itemFactory;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        KeyItemMainMenu mainMenu = new KeyItemMainMenu(plugin, registry, itemFactory, uiController);
        mainMenu.load();

        this.coreUI.registerMenu(KeyItemEditorUIKeys.MENU_MAIN, mainMenu);
    }
}
