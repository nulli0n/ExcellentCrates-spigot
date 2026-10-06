package su.nightexpress.excellentcrates.reward.crate.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIKeys;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.RewardEntryBrowseMenu;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.RewardEntryOptionsMenu;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.RewardEntrySelectMenu;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class RewardComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final RewardResolver                    rewardResolver;
    private final RewardPlaceholders                rewardPlaceholders;
    private final RewardPreviewService              previewService;
    private final RewardComponentEditorUIController uiController;

    public RewardComponentEditorMenuRegistrar(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              RewardResolver rewardResolver,
                                              RewardPlaceholders rewardPlaceholders,
                                              RewardPreviewService previewService,
                                              RewardComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.rewardResolver = rewardResolver;
        this.rewardPlaceholders = rewardPlaceholders;
        this.previewService = previewService;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        RewardEntryBrowseMenu browseMenu = new RewardEntryBrowseMenu(
            plugin, rewardResolver, previewService, uiController
        );

        RewardEntryOptionsMenu optionsMenu = new RewardEntryOptionsMenu(
            plugin, previewService, rewardPlaceholders, uiController
        );

        RewardEntrySelectMenu selectMenu = new RewardEntrySelectMenu(
            plugin, rewardResolver, previewService
        );

        browseMenu.load();
        optionsMenu.load();
        selectMenu.load();

        this.coreUI.registerMenu(RewardComponentEditorUIKeys.MENU_BROWSE, browseMenu);
        this.coreUI.registerMenu(RewardComponentEditorUIKeys.MENU_OPTIONS, optionsMenu);
        this.coreUI.registerMenu(RewardComponentEditorUIKeys.MENU_SELECT, selectMenu);
    }
}
