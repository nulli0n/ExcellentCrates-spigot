package su.nightexpress.excellentcrates.reward.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIKeys;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.PreviewLoreDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.PreviewNameDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardCreationDialog;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.RewardDeletionDialog;

@NullMarked
public class EditorUIDialogRegistrar implements StartupComponent {

    private final CoreUIService            coreUI;
    private final RewardEditorUIController uiController;

    public EditorUIDialogRegistrar(CoreUIService coreUI, RewardEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_CREATION, new RewardCreationDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_DELETION, new RewardDeletionDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_NAME, new PreviewNameDialog(uiController));
        coreUI.registerDialog(RewardEditorUIKeys.DIALOG_PREVIEW_LORE, new PreviewLoreDialog(uiController));
    }
}
