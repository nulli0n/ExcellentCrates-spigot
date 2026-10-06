package su.nightexpress.excellentcrates.crates.open.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIController;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIKeys;
import su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.CrateOpenActionsCommandsDialog;

@NullMarked
public class CrateOpeningEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                  coreUI;
    private final CrateOpeningEditorUIController uiController;

    public CrateOpeningEditorDialogRegistrar(CoreUIService coreUI,
                                             CrateOpeningEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateOpeningEditorUIKeys.DIALOG_COMMANDS,
            new CrateOpenActionsCommandsDialog(uiController)
        );
    }
}
