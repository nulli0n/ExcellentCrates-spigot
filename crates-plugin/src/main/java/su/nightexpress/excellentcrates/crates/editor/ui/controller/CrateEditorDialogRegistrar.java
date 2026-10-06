package su.nightexpress.excellentcrates.crates.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIKeys;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.CrateCreationDialog;
import su.nightexpress.excellentcrates.crates.editor.ui.dialog.CrateDeletionDialog;

@NullMarked
public class CrateEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService           coreUI;
    private final CrateEditorUIController uiController;

    public CrateEditorDialogRegistrar(CoreUIService coreUI, CrateEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateEditorUIKeys.DIALOG_CREATION, new CrateCreationDialog(this.uiController));
        this.coreUI.registerDialog(CrateEditorUIKeys.DIALOG_DELETION, new CrateDeletionDialog(this.uiController));
    }
}
