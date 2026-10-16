package su.nightexpress.excellentcrates.reward.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIKeys;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardDeletionDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardLoreDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardManualCreationDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardNameDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardWeightDialog;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;

@NullMarked
public class RewardEditorUIDialogRegistrar implements StartupComponent {

    private final CoreUIService            coreUI;
    private final RewardEvaluationService  evaluationService;
    private final RewardEditorUIController uiController;

    public RewardEditorUIDialogRegistrar(CoreUIService coreUI,
                                         RewardEvaluationService evaluationService,
                                         RewardEditorUIController uiController) {
        this.coreUI = coreUI;
        this.evaluationService = evaluationService;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardEditorUIKeys.DIALOG_WEIGHT,
            new RewardWeightDialog(evaluationService, uiController)
        );

        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_MANUAL_CREATION, new RewardManualCreationDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_DELETION, new RewardDeletionDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_NAME, new RewardNameDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_LORE, new RewardLoreDialog(uiController));
    }
}
