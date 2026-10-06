package su.nightexpress.excellentcrates.crates.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIKeys;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.CrateBrowseMenu;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.CrateOptionsMenu;

@NullMarked
public class CrateEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin      plugin;
    private final CoreUIService     coreUI;
    private final CrateRegistry     crateRegistry;
    private final ICrateItemFactory crateRenderer;
    private final CratePlaceholders cratePlaceholders;

    private final TinyRegistry<CrateEditorExtension> extensions;
    private final CrateEditorUIController            uiController;

    public CrateEditorMenuRegistrar(CratesPlugin plugin,
                                    CoreUIService coreUI,
                                    CrateRegistry crateRegistry,
                                    ICrateItemFactory crateRenderer,
                                    CratePlaceholders cratePlaceholders,
                                    TinyRegistry<CrateEditorExtension> extensions,
                                    CrateEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateRegistry = crateRegistry;
        this.crateRenderer = crateRenderer;
        this.cratePlaceholders = cratePlaceholders;
        this.extensions = extensions;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.loadMenus();
    }

    private void loadMenus() {
        CrateBrowseMenu browseMenu = new CrateBrowseMenu(
            plugin, uiController, crateRegistry, crateRenderer, cratePlaceholders
        );
        CrateOptionsMenu optionsMenu = new CrateOptionsMenu(plugin, crateRegistry, extensions, uiController);

        browseMenu.load();
        optionsMenu.load();

        this.coreUI.registerMenu(CrateEditorUIKeys.MENU_BROWSE, browseMenu);
        this.coreUI.registerMenu(CrateEditorUIKeys.MENU_OPTIONS, optionsMenu);
    }
}
