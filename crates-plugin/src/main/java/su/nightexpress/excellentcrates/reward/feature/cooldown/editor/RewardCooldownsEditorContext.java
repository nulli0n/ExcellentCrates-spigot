package su.nightexpress.excellentcrates.reward.feature.cooldown.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIService;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.controller.RewardCooldownsEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.controller.RewardCooldownsEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.extension.RewardCooldownsEditorExtension;

@NullMarked
public final class RewardCooldownsEditorContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.cooldown.ui");
    private static final String     NAME = "Editor";

    public final RewardEditorExtension editorExtension;

    public RewardCooldownsEditorContext(CratesPlugin plugin,
                                        CoreUIService coreUI,
                                        RewardMessageDispatcher dispatcher,
                                        RewardRegistry registry) {
        super(ID, NAME);

        RewardCooldownsEditorService editorService = new RewardCooldownsEditorService();
        RewardCooldownsEditorUIService uiService = new RewardCooldownsEditorUIService(coreUI);
        RewardCooldownsEditorUIController uiController = new RewardCooldownsEditorUIController(
            registry, editorService, uiService, dispatcher
        );

        this.editorExtension = new RewardCooldownsEditorExtension(uiController);

        this.addComponent(new RewardCooldownsEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new RewardCooldownsEditorMenuRegistrar(plugin, coreUI, uiController));
    }
}
