package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.RewardLimitsAlternativeMenu;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.RewardLimitsMainMenu;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class RewardLimitsEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                   plugin;
    private final RewardRegistry                 registry;
    private final RewardPreviewService           previewService;
    private final RewardPlaceholders             rewardPlaceholders;
    private final CoreUIService                  coreUI;
    private final RewardLimitsEditorUIController controller;

    public RewardLimitsEditorMenuRegistrar(CratesPlugin plugin,
                                           RewardRegistry registry,
                                           RewardPreviewService previewService,
                                           RewardPlaceholders rewardPlaceholders,
                                           CoreUIService coreUI,
                                           RewardLimitsEditorUIController controller) {
        this.plugin = plugin;
        this.registry = registry;
        this.previewService = previewService;
        this.rewardPlaceholders = rewardPlaceholders;
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        RewardLimitsMainMenu mainMenu = new RewardLimitsMainMenu(plugin, registry, previewService, controller);

        RewardLimitsAlternativeMenu altSelectionMenu = new RewardLimitsAlternativeMenu(
            plugin, registry, previewService, rewardPlaceholders, controller
        );

        mainMenu.load();
        altSelectionMenu.load();

        this.coreUI.registerMenu(RewardLimitsEditorUIKeys.MENU_MAIN, mainMenu);
        this.coreUI.registerMenu(RewardLimitsEditorUIKeys.MENU_ALTERNATIVE_SELECTION, altSelectionMenu);
    }
}
