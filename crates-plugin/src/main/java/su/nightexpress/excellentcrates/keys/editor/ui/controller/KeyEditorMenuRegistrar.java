package su.nightexpress.excellentcrates.keys.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIKeys;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.KeyBrowseMenu;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.KeySettingsMenu;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;

@NullMarked
public class KeyEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                     plugin;
    private final CoreUIService                    coreUI;
    private final KeyRegistry                      registry;
    private final KeyItemFactory                   itemFactory;
    private final TinyRegistry<KeyEditorExtension> extensions;
    private final KeyEditorUIController            controller;

    public KeyEditorMenuRegistrar(CratesPlugin plugin,
                                  CoreUIService coreUI,
                                  KeyRegistry registry,
                                  KeyItemFactory itemFactory,
                                  TinyRegistry<KeyEditorExtension> extensions,
                                  KeyEditorUIController controller) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.itemFactory = itemFactory;
        this.extensions = extensions;
        this.controller = controller;
    }

    @Override
    public void start() {
        KeyBrowseMenu browseMenu = new KeyBrowseMenu(plugin, registry, itemFactory, controller);
        KeySettingsMenu settingsMenu = new KeySettingsMenu(plugin, registry, extensions, controller);

        browseMenu.load();
        settingsMenu.load();

        this.coreUI.registerMenu(KeyEditorUIKeys.MENU_BROWSE, browseMenu);
        this.coreUI.registerMenu(KeyEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
