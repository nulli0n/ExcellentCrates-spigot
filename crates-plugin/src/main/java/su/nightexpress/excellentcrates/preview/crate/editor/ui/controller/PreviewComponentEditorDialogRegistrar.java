package su.nightexpress.excellentcrates.preview.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIController;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIKeys;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.dialog.PreviewComponentSelectionDialog;

@NullMarked
public class PreviewComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                      coreUI;
    private final PreviewRegistry                    previewRegistry;
    private final PreviewComponentEditorUIController uiController;

    public PreviewComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                 PreviewRegistry previewRegistry,
                                                 PreviewComponentEditorUIController uiController) {
        this.coreUI = coreUI;
        this.previewRegistry = previewRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(PreviewComponentEditorUIKeys.DIALOG_SELECTION,
            new PreviewComponentSelectionDialog(this.previewRegistry, this.uiController)
        );
    }
}
