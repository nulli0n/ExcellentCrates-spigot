package su.nightexpress.excellentcrates.crates.hologram.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIKeys;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.CrateHologramOffsetDialog;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.CrateHologramTextDialog;

@NullMarked
public class CrateHologramEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                   coreUI;
    private final CrateHologramEditorUIController controller;

    public CrateHologramEditorDialogRegistrar(CoreUIService coreUI, CrateHologramEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateHologramEditorUIKeys.DIALOG_HOLOGRAM_TEXT,
            new CrateHologramTextDialog(controller)
        );

        this.coreUI.registerDialog(CrateHologramEditorUIKeys.DIALOG_HOLOGRAM_OFFSET,
            new CrateHologramOffsetDialog(controller)
        );
    }
}
