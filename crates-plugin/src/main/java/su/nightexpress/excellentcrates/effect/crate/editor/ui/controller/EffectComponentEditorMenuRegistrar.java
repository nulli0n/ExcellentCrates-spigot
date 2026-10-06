package su.nightexpress.excellentcrates.effect.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIKeys;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.menu.EffectComponentEditorMainMenu;

@NullMarked
public class EffectComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final EffectRegistry                    effectRegistry;
    private final EffectComponentEditorUIController uiController;

    public EffectComponentEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              EffectRegistry effectRegistry,
                                              EffectComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.effectRegistry = effectRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        EffectComponentEditorMainMenu mainMenu = new EffectComponentEditorMainMenu(
            plugin, effectRegistry, uiController
        );

        mainMenu.load();

        this.coreUI.registerMenu(EffectComponentEditorUIKeys.MENU_MAIN, mainMenu);
    }
}
