package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIKeys;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.RewardRollCountDialog;

@NullMarked
public class RewardComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                     coreUI;
    private final RewardComponentEditorUIController controller;

    public RewardComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                RewardComponentEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RewardComponentEditorUIKeys.DIALOG_ROLL_COUNT,
            new RewardRollCountDialog(controller)
        );
    }
}
