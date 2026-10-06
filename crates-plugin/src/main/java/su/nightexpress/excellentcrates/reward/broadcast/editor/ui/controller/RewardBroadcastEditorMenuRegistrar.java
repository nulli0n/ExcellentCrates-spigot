package su.nightexpress.excellentcrates.reward.broadcast.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIController;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIKeys;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.BroadcastSettingsMenu;

@NullMarked
public class RewardBroadcastEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final RewardBroadcastEditorUIController uiController;

    public RewardBroadcastEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              RewardBroadcastEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        BroadcastSettingsMenu settingsMenu = new BroadcastSettingsMenu(this.plugin, this.uiController);

        settingsMenu.load();

        this.coreUI.registerMenu(RewardBroadcastEditorUIKeys.MENU_SETTINGS, settingsMenu);
    }
}
