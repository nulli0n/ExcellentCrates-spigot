package su.nightexpress.excellentcrates.keys.cost.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIKeys;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.KeyCostEditorEntriesMenu;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.KeyCostEditorEntryMenu;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;

@NullMarked
public class KeyCostEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin              plugin;
    private final CoreUIService             coreUI;
    private final KeyRegistry               keyRegistry;
    private final KeyItemFactory            keyItemFactory;
    private final KeyCostEditorUIController uiController;

    public KeyCostEditorMenuRegistrar(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      KeyRegistry keyRegistry,
                                      KeyItemFactory keyItemFactory,
                                      KeyCostEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.keyRegistry = keyRegistry;
        this.keyItemFactory = keyItemFactory;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        KeyCostEditorEntriesMenu entriesMenu = new KeyCostEditorEntriesMenu(
            plugin, keyRegistry, keyItemFactory, uiController
        );

        KeyCostEditorEntryMenu entryMenu = new KeyCostEditorEntryMenu(plugin, uiController);

        entriesMenu.load();
        entryMenu.load();

        this.coreUI.registerMenu(KeyCostEditorUIKeys.MENU_ENTRIES, entriesMenu);
        this.coreUI.registerMenu(KeyCostEditorUIKeys.MENU_ENTRY, entryMenu);
    }
}
