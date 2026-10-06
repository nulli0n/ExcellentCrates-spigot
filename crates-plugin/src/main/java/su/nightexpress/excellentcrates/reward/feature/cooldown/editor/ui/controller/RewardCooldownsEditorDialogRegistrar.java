package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.RewardCooldownsSettingsDialog;

@NullMarked
public class RewardCooldownsEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                     coreUI;
    private final RewardCooldownsEditorUIController uiController;

    public RewardCooldownsEditorDialogRegistrar(CoreUIService coreUI,
                                                RewardCooldownsEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardCooldownsEditorUIKeys.DIALOG_OPTIONS,
            new RewardCooldownsSettingsDialog(this.uiController)
        );
    }
}
