package su.nightexpress.excellentcrates.reward.crate.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIService;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.controller.RewardComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.controller.RewardComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.extension.RewardComponentEditorExtension;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public final class RewardComponentEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.component.editor");
    private static final String     NAME = "Component Editor";

    public final CrateEditorExtension editorExtension;

    public RewardComponentEditorContext(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        CrateMessageDispatcher dispatcher,
                                        CrateRegistry crateRegistry,
                                        RewardRegistry rewardRegistry,
                                        RewardPlaceholders rewardPlaceholders,
                                        RewardPreviewService previewService,
                                        RewardEditorUIService editorUIService) {
        super(ID, NAME);

        RewardComponentEditorService editorService = new RewardComponentEditorService();

        RewardComponentEditorUIService uiService = new RewardComponentEditorUIService(coreUI);
        RewardComponentEditorUIController uiController = new RewardComponentEditorUIController(
            crateRegistry, rewardRegistry, editorUIService, editorService, uiService, dispatcher
        );

        this.addComponent(new RewardComponentEditorDialogRegistrar(coreUI, rewardPlaceholders, uiController));

        this.addComponent(new RewardComponentEditorMenuRegistrar(
            plugin, coreUI, rewardRegistry, rewardPlaceholders, previewService, uiController)
        );

        this.editorExtension = new RewardComponentEditorExtension(uiController);
    }
}
