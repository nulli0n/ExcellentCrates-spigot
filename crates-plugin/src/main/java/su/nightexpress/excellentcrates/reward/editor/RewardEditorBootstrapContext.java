package su.nightexpress.excellentcrates.reward.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.RewardEditorUIDialogRegistrar;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.RewardEditorUIMenuLoader;
import su.nightexpress.excellentcrates.reward.id.RewardIdService;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;

@NullMarked
public class RewardEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.editor");
    private static final String     NAME = "Rewards Editor";

    public final RewardEditorService editorService;
    public final RewardIdService     idService;

    public final TinyRegistry<RewardEditorExtension> extensions;
    public final RewardEditorUIService               uiService;

    public final RewardEditorAPI api;

    public RewardEditorBootstrapContext(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        CratePlaceholders cratePlaceholders,
                                        RewardMessageDispatcher dispatcher,
                                        RewardRegistry registry,
                                        RewardDataService dataService,
                                        RewardPlaceholders rewardPlaceholders,
                                        RewardPreviewService previewService) {
        super(ID, NAME);

        plugin.injectLang(RewardEditorLang.class);

        this.editorService = new RewardEditorService(dataService, rewardPlaceholders);
        this.idService = new RewardIdService(registry);

        this.extensions = new SimpleRegistry<>();

        this.uiService = new RewardEditorUIService(coreUI);
        RewardEditorUIController uiController = new RewardEditorUIController(
            registry, idService, editorService, uiService, dispatcher
        );

        this.api = new DefaultRewardsEditorAPI(extensions, editorService, uiService);

        this.addComponent(
            new RewardEditorUIDialogRegistrar(coreUI, cratePlaceholders, rewardPlaceholders, uiController)
        );

        this.addComponent(
            new RewardEditorUIMenuLoader(plugin, coreUI, registry, previewService, uiController, extensions)
        );
    }
}
