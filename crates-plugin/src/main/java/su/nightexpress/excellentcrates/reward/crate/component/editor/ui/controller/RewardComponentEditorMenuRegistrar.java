package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIKeys;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.RewardComponentSettingsMenu;

@NullMarked
public class RewardComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final RewardComponentEditorUIController uiController;

    public RewardComponentEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              RewardComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        RewardComponentSettingsMenu settingsMenu = new RewardComponentSettingsMenu(plugin, uiController);

        settingsMenu.load();

        this.coreUI.registerMenu(RewardComponentEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
