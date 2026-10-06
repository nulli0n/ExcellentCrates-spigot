package su.nightexpress.excellentcrates.reward.items.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIController;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.items.editor.ui.menu.RewardItemsMenu;

@NullMarked
public class RewardItemsEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                  plugin;
    private final CoreUIService                 coreUI;
    private final RewardItemsEditorUIController uiController;

    public RewardItemsEditorMenuRegistrar(CratesPlugin plugin,
                                          CoreUIService coreUI,
                                          RewardItemsEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        RewardItemsMenu itemsMenu = new RewardItemsMenu(plugin, uiController);

        itemsMenu.load();

        this.coreUI.registerMenu(RewardItemsEditorUIKeys.MENU_ITEMS, itemsMenu);
    }

}
