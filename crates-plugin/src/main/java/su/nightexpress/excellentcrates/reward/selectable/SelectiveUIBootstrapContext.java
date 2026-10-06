package su.nightexpress.excellentcrates.reward.selectable;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.excellentcrates.reward.selectable.pipeline.RewardSelectionPipelineStage;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIController;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIService;
import su.nightexpress.excellentcrates.reward.selectable.ui.controller.SelectiveUIMenuRegistrar;

@NullMarked
public class SelectiveUIBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.selection");
    private static final String     NAME = "Selective Rewards UI";

    private final RewardSelectionPipelineStage pipelineStage;

    public SelectiveUIBootstrapContext(CratesPlugin plugin,
                                       CoreUIService coreUI,
                                       CrateRegistry crates,
                                       RewardMessageDispatcher dispatcher,
                                       RewardPlaceholders placeholders,
                                       RewardPreviewService previewService,
                                       SelectivePickService pickService) {
        super(ID, NAME);

        SelectiveUIService uiService = new SelectiveUIService(coreUI);
        SelectiveUIController uiController = new SelectiveUIController(crates, uiService, pickService, dispatcher);

        this.pipelineStage = new RewardSelectionPipelineStage(uiController);

        this.addComponent(
            new SelectiveUIMenuRegistrar(plugin, coreUI, previewService, placeholders, pickService, uiController)
        );
    }

    public RewardSelectionPipelineStage getPipelineStage() {
        return this.pipelineStage;
    }
}
