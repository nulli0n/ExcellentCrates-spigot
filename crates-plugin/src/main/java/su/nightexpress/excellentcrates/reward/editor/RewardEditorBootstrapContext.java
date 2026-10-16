package su.nightexpress.excellentcrates.reward.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.sort.RewardChanceSorter;
import su.nightexpress.excellentcrates.reward.editor.sort.RewardIdSorter;
import su.nightexpress.excellentcrates.reward.editor.sort.RewardWeightSorter;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.RewardEditorUIDialogRegistrar;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.RewardEditorUIMenuLoader;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;
import su.nightexpress.excellentcrates.reward.id.RewardIdService;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class RewardEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.editor");
    private static final String     NAME = "Rewards Editor";

    public final RewardEditorService editorService;
    public final RewardIdService     idService;

    public final IdentifiableRegistry<RewardSorter>  sorters;
    public final TinyRegistry<RewardEditorExtension> extensions;
    public final RewardEditorUIService               uiService;

    public final RewardEditorAPI api;

    public RewardEditorBootstrapContext(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        RewardMessageDispatcher dispatcher,
                                        RewardRegistry registry,
                                        RewardDataService dataService,
                                        RewardEvaluationService evaluationService,
                                        RewardPreviewService previewService) {
        super(ID, NAME);
        this.extensions = new SimpleRegistry<>();
        this.sorters = new IdentifiableRegistry<>();

        plugin.injectLang(RewardEditorLang.class);

        this.editorService = new RewardEditorService(dataService, this.sorters);
        this.idService = new RewardIdService(registry);

        this.uiService = new RewardEditorUIService(coreUI);
        RewardEditorUIController uiController = new RewardEditorUIController(
            registry, idService, editorService, uiService, dispatcher
        );

        this.api = new DefaultRewardsEditorAPI(extensions, sorters);

        this.sorters.register(new RewardChanceSorter(evaluationService));
        this.sorters.register(new RewardIdSorter());
        this.sorters.register(new RewardWeightSorter());

        this.addComponent(
            new RewardEditorUIDialogRegistrar(coreUI, sorters, evaluationService, uiController)
        );

        this.addComponent(
            new RewardEditorUIMenuLoader(plugin, coreUI, registry, previewService, uiController, extensions)
        );
    }
}
