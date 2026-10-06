package su.nightexpress.excellentcrates.animation.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIController;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIKeys;
import su.nightexpress.excellentcrates.animation.component.editor.ui.dialog.AnimationComponentEditorSelectionDialog;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;

@NullMarked
public class AnimationComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                        coreUI;
    private final AnimationRegistry                    openingRegistry;
    private final AnimationComponentEditorUIController uiController;

    public AnimationComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                   AnimationRegistry openingRegistry,
                                                   AnimationComponentEditorUIController uiController) {
        this.coreUI = coreUI;
        this.openingRegistry = openingRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(AnimationComponentEditorUIKeys.DIALOG_SELECTION,
            new AnimationComponentEditorSelectionDialog(openingRegistry, uiController)
        );
    }

}
