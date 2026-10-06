package su.nightexpress.excellentcrates.preview.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIKeys;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIController;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.PreviewComponentEditorMainMenu;

@NullMarked
public class PreviewComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                       plugin;
    private final CoreUIService                      coreUI;
    private final PreviewRegistry                    previews;
    private final PreviewComponentEditorUIController uiController;

    public PreviewComponentEditorMenuRegistrar(CratesPlugin plugin,
                                               CoreUIService coreUI,
                                               PreviewRegistry previews,
                                               PreviewComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.previews = previews;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        PreviewComponentEditorMainMenu mainMenu = new PreviewComponentEditorMainMenu(
            plugin, previews, uiController
        );

        mainMenu.load();

        this.coreUI.registerMenu(PreviewComponentEditorUIKeys.MENU_MAIN, mainMenu);
    }
}
