package su.nightexpress.excellentcrates.reward.broadcast;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.reward.broadcast.component.DefaultRewardBroadcastComponent;
import su.nightexpress.excellentcrates.reward.broadcast.component.codec.RewardBroadcastComponentCodec;
import su.nightexpress.excellentcrates.reward.broadcast.component.extension.RewardBroadcastDataExtension;
import su.nightexpress.excellentcrates.reward.broadcast.editor.RewardBroadcastEditorService;
import su.nightexpress.excellentcrates.reward.broadcast.editor.extension.RewardBroadcastEditorExtension;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIController;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIService;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.controller.RewardBroadcastEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.broadcast.grant.RewardBroadcastGrantProcessor;
import su.nightexpress.excellentcrates.reward.broadcast.lang.RewardBroadcastLang;
import su.nightexpress.excellentcrates.reward.broadcast.message.BroadcastMessage;
import su.nightexpress.excellentcrates.reward.broadcast.message.BroadcastMessageService;
import su.nightexpress.excellentcrates.reward.broadcast.message.codec.BroadcastMessageCodec;
import su.nightexpress.excellentcrates.reward.broadcast.settings.BroadcastSettings;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardBroadcastBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.broadcast");
    private static final String     NAME = "Broadcast";

    private static final String SETTINGS_FILE_NAME = "rewards.broadcast.yml";

    private final RewardBroadcastDataExtension   dataExtension;
    private final RewardBroadcastEditorExtension editorExtension;
    private final RewardBroadcastGrantProcessor  grantProcessor;

    public RewardBroadcastBootstrapContext(CratesPlugin plugin,
                                           CoreUIService coreUI,
                                           RewardMessageDispatcher dispatcher,
                                           RewardRegistry rewardRegistry) {
        super(ID, NAME);

        plugin.injectLang(RewardBroadcastLang.class);

        ConfigCodecs.register(DefaultRewardBroadcastComponent.class, RewardBroadcastComponentCodec.INSTANCE);
        ConfigCodecs.register(BroadcastMessage.class, BroadcastMessageCodec.INSTANCE);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<BroadcastSettings> settings = new SettingsProvider<>(BroadcastSettings.defaults());

        RewardBroadcastEditorService editorService = new RewardBroadcastEditorService();
        RewardBroadcastEditorUIService uiService = new RewardBroadcastEditorUIService(coreUI);
        RewardBroadcastEditorUIController uiController = new RewardBroadcastEditorUIController(
            rewardRegistry, editorService, uiService, dispatcher
        );

        BroadcastMessageService messageService = new BroadcastMessageService(settings);

        this.dataExtension = new RewardBroadcastDataExtension();
        this.editorExtension = new RewardBroadcastEditorExtension(uiController);
        this.grantProcessor = new RewardBroadcastGrantProcessor(messageService);

        this.addComponent(new SettingsController<>(settingsPath, BroadcastSettings::loadFrom, settings));
        this.addComponent(new RewardBroadcastEditorMenuRegistrar(plugin, coreUI, uiController));
    }

    public RewardBroadcastDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public RewardBroadcastEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public RewardBroadcastGrantProcessor getGrantProcessor() {
        return this.grantProcessor;
    }
}

