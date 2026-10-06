package su.nightexpress.excellentcrates.reward.feature.commands;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.commands.component.StandrdRewardCommandPool;
import su.nightexpress.excellentcrates.reward.feature.commands.component.codec.RewardCommandBundleCodec;
import su.nightexpress.excellentcrates.reward.feature.commands.component.codec.RewardCommandContentCodec;
import su.nightexpress.excellentcrates.reward.feature.commands.component.data.DefaultRewardCommandsComponent;
import su.nightexpress.excellentcrates.reward.feature.commands.component.extension.RewardCommandsDataExtension;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.RewardCommandsEditorService;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIService;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.controller.RewardCommandsEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.controller.RewardCommandsEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.extension.RewardCommandsEditorExtension;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.excellentcrates.reward.feature.commands.processor.RewardCommandsGrantProcessor;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardCommandsBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.commands");
    private static final String     NAME = "Content - Commands";

    private final RewardDataExtension   dataExtension;
    private final RewardEditorExtension editorExtension;
    private final RewardGrantProcessor  grantProcessor;

    public RewardCommandsBootstrapContext(CratesPlugin plugin,
                                          CoreUIService uiService,
                                          MessageDispatcher dispatcher,
                                          RewardRegistry rewardRegistry) {
        super(ID, NAME);

        ConfigCodecs.register(DefaultRewardCommandsComponent.class, RewardCommandContentCodec.INSTANCE);
        ConfigCodecs.register(StandrdRewardCommandPool.class, RewardCommandBundleCodec.INSTANCE);

        plugin.injectLang(RewardCommandsLang.class);

        RewardCommandsEditorService editorService = new RewardCommandsEditorService();
        RewardCommandsEditorUIService editorUIService = new RewardCommandsEditorUIService(uiService);
        RewardCommandsEditorUIController editorUIController = new RewardCommandsEditorUIController(
            editorService, editorUIService, dispatcher
        );

        this.dataExtension = new RewardCommandsDataExtension();
        this.editorExtension = new RewardCommandsEditorExtension(editorUIController);
        this.grantProcessor = new RewardCommandsGrantProcessor();

        this.addComponent(new RewardCommandsEditorMenuRegistrar(plugin, uiService, rewardRegistry, editorUIController));
        this.addComponent(new RewardCommandsEditorDialogRegistrar(uiService, editorUIController));
    }

    public RewardDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public RewardEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public RewardGrantProcessor getGrantProcessor() {
        return this.grantProcessor;
    }
}
