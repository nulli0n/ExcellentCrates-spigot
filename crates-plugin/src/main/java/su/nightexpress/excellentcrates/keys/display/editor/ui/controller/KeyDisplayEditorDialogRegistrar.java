package su.nightexpress.excellentcrates.keys.display.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIKeys;
import su.nightexpress.excellentcrates.keys.display.editor.ui.dialog.KeyDisplayLoreDialog;
import su.nightexpress.excellentcrates.keys.display.editor.ui.dialog.KeyDisplayNameDialog;

@NullMarked
public class KeyDisplayEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                coreUI;
    private final KeyDisplayEditorUIController controller;

    public KeyDisplayEditorDialogRegistrar(CoreUIService coreUI,
                                           KeyDisplayEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(KeyDisplayEditorUIKeys.DIALOG_NAME, new KeyDisplayNameDialog(controller));
        this.coreUI.registerDialog(KeyDisplayEditorUIKeys.DIALOG_LORE, new KeyDisplayLoreDialog(controller));
    }
}
