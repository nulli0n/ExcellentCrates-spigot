package su.nightexpress.excellentcrates.reward.feature.limit.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIService;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.controller.RewardLimitsEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.controller.RewardLimitsEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.extension.RewardLimitsEditorExtension;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class RewardLimitsEditorContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("rewards.limits.editor");
    private static final String     BUNDLE_NAME = "Editor";

    public final RewardEditorExtension editorExtension;

    public RewardLimitsEditorContext(CratesPlugin plugin,
                                     CoreUIService coreUI,
                                     MessageDispatcher dispatcher,
                                     RewardRegistry registry,
                                     RewardPreviewService viewService,
                                     RewardPlaceholders rewardPlaceholders) {
        super(BUNDLE_ID, BUNDLE_NAME);

        RewardLimitsEditorService editorService = new RewardLimitsEditorService(rewardPlaceholders);
        RewardLimitsEditorUIService uiService = new RewardLimitsEditorUIService(coreUI);
        RewardLimitsEditorUIController uiController = new RewardLimitsEditorUIController(
            editorService, uiService, dispatcher
        );

        this.editorExtension = new RewardLimitsEditorExtension(uiController);

        this.addComponent(new RewardLimitsEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(
            new RewardLimitsEditorMenuRegistrar(plugin, registry, viewService, rewardPlaceholders, coreUI, uiController)
        );
    }
}
