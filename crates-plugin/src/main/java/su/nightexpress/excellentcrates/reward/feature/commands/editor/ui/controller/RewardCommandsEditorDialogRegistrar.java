package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.RewardCommandsPoolDeleteDialog;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.RewardCommandsBundleDialog;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.RewardCommandsGiveModeDialog;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.RewardCommandsIterationsDialog;

@NullMarked
public class RewardCommandsEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                    coreUI;
    private final RewardCommandsEditorUIController uiController;

    public RewardCommandsEditorDialogRegistrar(CoreUIService coreUI, RewardCommandsEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        coreUI.registerDialog(RewardCommandsEditorUIKeys.DIALOG_ITERATIONS,
            new RewardCommandsIterationsDialog(uiController)
        );

        coreUI.registerDialog(RewardCommandsEditorUIKeys.DIALOG_GIVE_MODE,
            new RewardCommandsGiveModeDialog(uiController)
        );

        coreUI.registerDialog(RewardCommandsEditorUIKeys.DIALOG_BUNDLE,
            new RewardCommandsBundleDialog(uiController)
        );

        coreUI.registerDialog(RewardCommandsEditorUIKeys.DIALOG_BUNDLE_DELETE,
            new RewardCommandsPoolDeleteDialog(uiController)
        );
    }

}
