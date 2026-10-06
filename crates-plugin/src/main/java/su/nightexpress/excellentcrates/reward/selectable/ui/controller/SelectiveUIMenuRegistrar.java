package su.nightexpress.excellentcrates.reward.selectable.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickService;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIController;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIKeys;
import su.nightexpress.excellentcrates.reward.selectable.ui.menu.RewardSelectionMenu;

@NullMarked
public class SelectiveUIMenuRegistrar extends BasePluginComponent {

    private final CratesPlugin          plugin;
    private final CoreUIService         coreUI;
    private final RewardPreviewService  previewService;
    private final RewardPlaceholders    placeholders;
    private final SelectivePickService  pickService;
    private final SelectiveUIController uiController;

    public SelectiveUIMenuRegistrar(CratesPlugin plugin,
                                    CoreUIService coreUI,
                                    RewardPreviewService previewService,
                                    RewardPlaceholders placeholders,
                                    SelectivePickService pickService,
                                    SelectiveUIController uiController) {
        super();
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.previewService = previewService;
        this.placeholders = placeholders;
        this.pickService = pickService;
        this.uiController = uiController;
    }

    @Override
    protected void onReload() {
        this.shutdown();
        this.start();
    }

    @Override
    protected void onShutdown() {
        this.coreUI.unregisterMenu(SelectiveUIKeys.MENU_SELECTION);
    }

    @Override
    protected void onStart() {
        this.registerMenus();
    }

    private void registerMenus() {
        RewardSelectionMenu selectionMenu = new RewardSelectionMenu(
            plugin, previewService, placeholders, pickService, uiController
        );

        selectionMenu.load(plugin.menuPath().resolve("reward_selection.yml"));

        this.coreUI.registerMenu(SelectiveUIKeys.MENU_SELECTION, selectionMenu);
    }
}
