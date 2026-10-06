package su.nightexpress.excellentcrates.crates.cooldown.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIController;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIKeys;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.dialog.CrateCooldownsSettingsDialog;

@NullMarked
public class CrateCooldownsEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                    coreUI;
    private final CrateCooldownsEditorUIController uiController;

    public CrateCooldownsEditorDialogRegistrar(CoreUIService coreUI,
                                               CrateCooldownsEditorUIController uiController) {
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(CrateCooldownsEditorUIKeys.DIALOG_COOLDOWN_SETTINGS,
            new CrateCooldownsSettingsDialog(this.uiController)
        );
    }
}
