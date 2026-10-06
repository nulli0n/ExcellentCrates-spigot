package su.nightexpress.excellentcrates.reward.items;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.items.component.DefaultRewardItemsComponent;
import su.nightexpress.excellentcrates.reward.items.component.codec.RewardItemsComponentCodec;
import su.nightexpress.excellentcrates.reward.items.component.extension.RewardItemsDataExtension;
import su.nightexpress.excellentcrates.reward.items.editor.RewardItemsEditorService;
import su.nightexpress.excellentcrates.reward.items.editor.extension.RewardItemsEditorExtension;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIController;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIService;
import su.nightexpress.excellentcrates.reward.items.editor.ui.controller.RewardItemsEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.items.editor.ui.controller.RewardItemsEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.items.lang.RewardItemsLang;
import su.nightexpress.excellentcrates.reward.items.processor.RewardItemsGrantProcessor;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardItemsBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.items");
    private static final String     NAME = "Content - Items";

    private final RewardItemsDataExtension   dataExtension;
    private final RewardItemsEditorExtension editorExtension;
    private final RewardItemsGrantProcessor  grantProcessor;

    public RewardItemsBootstrapContext(CratesPlugin plugin,
                                       CoreUIService coreUI,
                                       RewardMessageDispatcher dispatcher,
                                       RewardRegistry rewardRegistry) {
        super(ID, NAME);

        plugin.injectLang(RewardItemsLang.class);

        ConfigCodecs.register(DefaultRewardItemsComponent.class, RewardItemsComponentCodec.INSTANCE);

        RewardItemsEditorService editorService = new RewardItemsEditorService();
        RewardItemsEditorUIService editorUIService = new RewardItemsEditorUIService(coreUI);
        RewardItemsEditorUIController editorUIController = new RewardItemsEditorUIController(
            rewardRegistry, editorService, editorUIService, dispatcher
        );

        this.dataExtension = new RewardItemsDataExtension();
        this.editorExtension = new RewardItemsEditorExtension(editorUIController);
        this.grantProcessor = new RewardItemsGrantProcessor();

        this.addComponent(new RewardItemsEditorDialogRegistrar(coreUI, editorUIController));
        this.addComponent(new RewardItemsEditorMenuRegistrar(plugin, coreUI, editorUIController));
    }

    public RewardItemsDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public RewardItemsEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public RewardItemsGrantProcessor getGrantProcessor() {
        return this.grantProcessor;
    }
}
