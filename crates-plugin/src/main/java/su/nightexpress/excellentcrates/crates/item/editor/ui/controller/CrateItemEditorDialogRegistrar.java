package su.nightexpress.excellentcrates.crates.item.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIController;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIKeys;
import su.nightexpress.excellentcrates.crates.item.editor.ui.dialog.CrateItemIconDialog;

@NullMarked
public class CrateItemEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService               coreUI;
    private final CrateItemEditorUIController controller;

    public CrateItemEditorDialogRegistrar(CoreUIService coreUI, CrateItemEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateItemEditorUIKeys.DIALOG_ICON, new CrateItemIconDialog(this.controller));
    }
}
