package su.nightexpress.excellentcrates.rarity.reward.component.editor.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityResolver;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIKeys;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu.RarityComponentEditorMainMenu;

@NullMarked
public class RarityComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final RarityResolver                    rarityResolver;
    private final RarityComponentEditorUIController uiController;

    public RarityComponentEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              RarityResolver rarityResolver,
                                              RarityComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.rarityResolver = rarityResolver;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        RarityComponentEditorMainMenu mainMenu = new RarityComponentEditorMainMenu(
            plugin, rarityResolver, uiController
        );

        mainMenu.load();

        this.coreUI.registerMenu(RarityComponentEditorUIKeys.MENU_MAIN, mainMenu);
    }
}
