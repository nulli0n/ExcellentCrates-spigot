package su.nightexpress.excellentcrates.reward.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIKeys;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.RewardWeightDialog;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.dialog.RewardsRequiredAmountDialog;

@NullMarked
public class RewardComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                     coreUI;
    private final RewardPlaceholders                rewardPlaceholders;
    private final RewardComponentEditorUIController controller;

    public RewardComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                RewardPlaceholders rewardPlaceholders,
                                                RewardComponentEditorUIController controller) {
        this.coreUI = coreUI;
        this.rewardPlaceholders = rewardPlaceholders;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardComponentEditorUIKeys.DIALOG_WEIGHT,
            new RewardWeightDialog(rewardPlaceholders, controller)
        );

        this.coreUI.registerDialog(RewardComponentEditorUIKeys.DIALOG_REQUIRED_AMOUNT,
            new RewardsRequiredAmountDialog(controller)
        );
    }
}
