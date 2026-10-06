package su.nightexpress.excellentcrates.reward.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommand;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.command.RewardEditorCommand;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.EditorUIDialogRegistrar;
import su.nightexpress.excellentcrates.reward.editor.ui.controller.EditorUIMenuLoader;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.EditorPreferences;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.InMemoryPreferencesSessionManager;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.PreferencesFactory;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.PreferencesSessionController;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.PreferencesSessionManager;
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

    public final RewardCommand editorCommand;

    public final RewardEditorAPI api;

    public RewardEditorBootstrapContext(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        RewardMessageDispatcher dispatcher,
                                        RewardRegistry registry,
                                        RewardDataService dataService,
                                        RewardPlaceholders rewardPlaceholders,
                                        RewardPreviewService previewService) {
        super(ID, NAME);

        plugin.injectLang(RewardEditorLang.class);

        this.editorService = new RewardEditorService(dataService, rewardPlaceholders);
        this.idService = new RewardIdService(registry);

        PreferencesFactory factory = () -> new EditorPreferences(false);
        PreferencesSessionManager sessionManager = new InMemoryPreferencesSessionManager(factory);
        PreferencesSessionController sessionController = new PreferencesSessionController(plugin, sessionManager);

        this.extensions = new SimpleRegistry<>();

        this.uiService = new RewardEditorUIService(coreUI, sessionManager);
        RewardEditorUIController uiController = new RewardEditorUIController(
            idService, editorService, this.uiService, dispatcher);

        this.editorCommand = new RewardEditorCommand(uiService, dispatcher);
        this.api = new DefaultRewardsEditorAPI(extensions, editorService, uiService);

        this.addComponent(new EditorUIDialogRegistrar(coreUI, uiController));
        this.addComponent(
            new EditorUIMenuLoader(
                plugin, coreUI, registry, previewService, uiController, extensions
            )
        );
        this.addComponent(sessionController);
    }
}
