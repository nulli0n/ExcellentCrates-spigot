package su.nightexpress.excellentcrates.crates.display.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.display.editor.ui.CrateDisplayEditorUIKeys;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.CrateDisplayLoreDialog;
import su.nightexpress.excellentcrates.crates.display.editor.ui.dialog.CrateDisplayNameDialog;

@NullMarked
public class DisplayEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService             coreUI;
    private final DisplayEditorUIController controller;

    public DisplayEditorDialogRegistrar(CoreUIService coreUI, DisplayEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateDisplayEditorUIKeys.DIALOG_NAME, new CrateDisplayNameDialog(this.controller));
        this.coreUI.registerDialog(CrateDisplayEditorUIKeys.DIALOG_LORE, new CrateDisplayLoreDialog(this.controller));
    }
}
