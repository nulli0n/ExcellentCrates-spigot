package su.nightexpress.excellentcrates.reward.items.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIController;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.items.editor.ui.dialog.RewardItemAddDialog;

@NullMarked
public class RewardItemsEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                 coreUI;
    private final RewardItemsEditorUIController uiController;

    public RewardItemsEditorDialogRegistrar(CoreUIService coreUI, RewardItemsEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        coreUI.registerDialog(RewardItemsEditorUIKeys.DIALOG_ADD_ITEM, new RewardItemAddDialog(uiController));
    }
}