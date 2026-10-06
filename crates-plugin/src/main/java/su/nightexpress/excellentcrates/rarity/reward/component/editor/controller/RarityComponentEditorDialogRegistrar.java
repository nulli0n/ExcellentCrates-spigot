package su.nightexpress.excellentcrates.rarity.reward.component.editor.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIKeys;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.RarityComponentSelectionDialog;

@NullMarked
public class RarityComponentEditorDialogRegistrar implements StartupComponent {

    private final CoreUIService                     coreUI;
    private final RarityRegistry                    rarityRegistry;
    private final RarityComponentEditorUIController uiController;

    public RarityComponentEditorDialogRegistrar(CoreUIService coreUI,
                                                RarityRegistry rarityRegistry,
                                                RarityComponentEditorUIController uiController) {
        this.coreUI = coreUI;
        this.rarityRegistry = rarityRegistry;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(RarityComponentEditorUIKeys.DIALOG_SELECTION,
            new RarityComponentSelectionDialog(rarityRegistry, uiController)
        );
    }
}
