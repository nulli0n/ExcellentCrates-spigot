package su.nightexpress.excellentcrates.keys.item.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorDialogKeys;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIController;
import su.nightexpress.excellentcrates.keys.item.editor.ui.dialog.KeyItemStackDialog;

@NullMarked
public class KeyItemEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService             coreUI;
    private final KeyItemEditorUIController uiController;

    public KeyItemEditorDialogRegistrar(CoreUIService coreUI,
                                        KeyItemEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(KeyItemEditorDialogKeys.ITEM, new KeyItemStackDialog(this.uiController));
    }
}
