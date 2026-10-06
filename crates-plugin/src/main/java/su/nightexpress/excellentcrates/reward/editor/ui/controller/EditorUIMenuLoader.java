package su.nightexpress.excellentcrates.reward.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIKeys;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.RewardBrowseMenu;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.RewardOptionsMenu;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.RewardPreviewMenu;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class EditorUIMenuLoader implements StartupComponent {

    private final CratesPlugin                        plugin;
    private final CoreUIService                       coreUI;
    private final RewardRegistry                      registry;
    private final RewardPreviewService                previewService;
    private final RewardEditorUIController            uiController;
    private final TinyRegistry<RewardEditorExtension> extensions;

    public EditorUIMenuLoader(CratesPlugin plugin,
                              CoreUIService coreUI,
                              RewardRegistry registry,
                              RewardPreviewService previewService,
                              RewardEditorUIController uiController,
                              TinyRegistry<RewardEditorExtension> extensions) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.previewService = previewService;
        this.uiController = uiController;
        this.extensions = extensions;
    }

    @Override
    public void start() {
        RewardBrowseMenu rewardsMenu = new RewardBrowseMenu(
            plugin, registry, previewService, uiController);

        RewardOptionsMenu optionsMenu = new RewardOptionsMenu(
            plugin, registry, previewService, uiController, extensions
        );

        RewardPreviewMenu previewMenu = new RewardPreviewMenu(plugin, registry, uiController);

        rewardsMenu.load();
        optionsMenu.load();
        previewMenu.load();

        coreUI.registerMenu(RewardEditorUIKeys.MENU_BROWSE, rewardsMenu);
        coreUI.registerMenu(RewardEditorUIKeys.MENU_OPTIONS, optionsMenu);
        coreUI.registerMenu(RewardEditorUIKeys.MENU_PREVIEW, previewMenu);
    }
}
