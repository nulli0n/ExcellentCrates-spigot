package su.nightexpress.excellentcrates.effect.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIKeys;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.dialog.EffectComponentSelectionDialog;

@NullMarked
public class EffectComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                     coreUI;
    private final EffectRegistry                    effectRegistry;
    private final EffectComponentEditorUIController uiController;

    public EffectComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                EffectRegistry effectRegistry,
                                                EffectComponentEditorUIController uiController) {
        this.coreUI = coreUI;
        this.effectRegistry = effectRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(EffectComponentEditorUIKeys.DIALOG_SELECTION,
            new EffectComponentSelectionDialog(this.effectRegistry, this.uiController)
        );
    }
}
