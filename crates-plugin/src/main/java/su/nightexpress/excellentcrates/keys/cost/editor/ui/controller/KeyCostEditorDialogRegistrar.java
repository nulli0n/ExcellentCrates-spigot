package su.nightexpress.excellentcrates.keys.cost.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIKeys;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.KeyCostAddEntryDialog;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.KeyCostEntryAmountDialog;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.KeyCostEntryRemoveDialog;

@NullMarked
public class KeyCostEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService             coreUI;
    private final KeyRegistry               keyRegistry;
    private final KeyPlaceholders           keyPlaceholders;
    private final KeyCostEditorUIController uiController;

    public KeyCostEditorDialogRegistrar(CoreUIService coreUI,
                                        KeyRegistry keyRegistry,
                                        KeyPlaceholders keyPlaceholders,
                                        KeyCostEditorUIController uiController) {
        this.coreUI = coreUI;
        this.keyRegistry = keyRegistry;
        this.keyPlaceholders = keyPlaceholders;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(KeyCostEditorUIKeys.DIALOG_ADD_ENTRY,
            new KeyCostAddEntryDialog(keyRegistry, keyPlaceholders, uiController)
        );

        this.coreUI.registerDialog(KeyCostEditorUIKeys.DIALOG_ENTRY_AMOUNT,
            new KeyCostEntryAmountDialog(uiController)
        );

        this.coreUI.registerDialog(KeyCostEditorUIKeys.DIALOG_REMOVE_ENTRY,
            new KeyCostEntryRemoveDialog(uiController)
        );
    }
}
