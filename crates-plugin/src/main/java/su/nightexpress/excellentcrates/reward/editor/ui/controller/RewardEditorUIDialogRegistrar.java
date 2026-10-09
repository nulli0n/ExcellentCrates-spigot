package su.nightexpress.excellentcrates.reward.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIKeys;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardLoreDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardNameDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardManualCreationDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardDeletionDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardWeightDialog;

@NullMarked
public class RewardEditorUIDialogRegistrar implements StartupComponent {

    private final CoreUIService            coreUI;
    private final CratePlaceholders        cratePlaceholders;
    private final RewardPlaceholders       rewardPlaceholders;
    private final RewardEditorUIController uiController;

    public RewardEditorUIDialogRegistrar(CoreUIService coreUI,
                                         CratePlaceholders cratePlaceholders,
                                         RewardPlaceholders rewardPlaceholders,
                                         RewardEditorUIController uiController) {
        this.coreUI = coreUI;
        this.cratePlaceholders = cratePlaceholders;
        this.rewardPlaceholders = rewardPlaceholders;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardEditorUIKeys.DIALOG_WEIGHT,
            new RewardWeightDialog(cratePlaceholders, rewardPlaceholders, uiController)
        );

        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_MANUAL_CREATION, new RewardManualCreationDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_DELETION, new RewardDeletionDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_NAME, new RewardNameDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_LORE, new RewardLoreDialog(uiController));
    }
}
