package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.RewardLimitOptionsDialog;

@NullMarked
public class RewardLimitsEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                  coreUI;
    private final RewardLimitsEditorUIController controller;

    public RewardLimitsEditorDialogRegistrar(CoreUIService coreUI,
                                             RewardLimitsEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardLimitsEditorUIKeys.DIALOG_OPTIONS,
            new RewardLimitOptionsDialog(controller)
        );
    }
}
