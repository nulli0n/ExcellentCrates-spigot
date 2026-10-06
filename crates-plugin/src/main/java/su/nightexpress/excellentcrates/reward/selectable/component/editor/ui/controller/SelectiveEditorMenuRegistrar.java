package su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.SelectiveEditorUIController;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.SelectiveEditorUIKeys;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu.SelectiveEditorSettingsMenu;

@NullMarked
public class SelectiveEditorMenuRegistrar extends BasePluginComponent {

    private final CratesPlugin                plugin;
    private final CoreUIService               coreUI;
    private final SelectiveEditorUIController uiController;

    public SelectiveEditorMenuRegistrar(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        SelectiveEditorUIController uiController) {
        super();
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    protected void onReload() {
        this.shutdown();
        this.start();
    }

    @Override
    protected void onShutdown() {
        this.coreUI.unregisterMenu(SelectiveEditorUIKeys.SETTINGS);
    }

    @Override
    protected void onStart() {
        this.registerMenus();
    }

    private void registerMenus() {
        SelectiveEditorSettingsMenu settingsMenu = new SelectiveEditorSettingsMenu(plugin, uiController);

        settingsMenu.load();

        this.coreUI.registerMenu(SelectiveEditorUIKeys.SETTINGS, settingsMenu);
    }
}
