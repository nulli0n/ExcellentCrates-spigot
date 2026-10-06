package su.nightexpress.excellentcrates.keys.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIKeys;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.KeyCreationDialog;
import su.nightexpress.excellentcrates.keys.editor.ui.dialog.KeyDeletionDialog;

@NullMarked
public class KeyEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService         coreUI;
    private final KeyEditorUIController uiController;

    public KeyEditorDialogRegistrar(CoreUIService coreUI, KeyEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(KeyEditorUIKeys.DIALOG_CREATION, new KeyCreationDialog(uiController));
        this.coreUI.registerDialog(KeyEditorUIKeys.DIALOG_DELETION, new KeyDeletionDialog(uiController));
    }
}
