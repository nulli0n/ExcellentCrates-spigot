package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu.RewardCooldownsMenu;

@NullMarked
public class RewardCooldownsEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final RewardCooldownsEditorUIController uiController;

    public RewardCooldownsEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              RewardCooldownsEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        RewardCooldownsMenu cooldownsMenu = new RewardCooldownsMenu(this.plugin, this.uiController);

        cooldownsMenu.load();

        this.coreUI.registerMenu(RewardCooldownsEditorUIKeys.MENU_COOLDOWNS, cooldownsMenu);
    }

}
