package su.nightexpress.excellentcrates.reward.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardEntryCodec;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardsComponentCodec;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentEditorService;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIService;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.controller.RewardComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.controller.RewardComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.extension.RewardComponentEditorExtension;
import su.nightexpress.excellentcrates.reward.crate.component.extension.RewardComponentExtension;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.component.model.StandardRewardEntry;
import su.nightexpress.excellentcrates.reward.crate.component.model.StandardRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.placeholder.RewardComponentPlaceholder;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardComponentBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("reward.component");
    private static final String     NAME = "Crate Component";

    private final RewardComponentExtension       crateDataExtension;
    private final RewardComponentPlaceholder     cratePlaceholder;
    private final RewardComponentEditorExtension crateEditorExtension;

    public RewardComponentBootstrapContext(CratesPlugin plugin,
                                           CoreUIService coreUI,
                                           CrateMessageDispatcher dispatcher,
                                           CrateRegistry crates,
                                           RewardRegistry rewards,
                                           RewardDataService dataService,
                                           RewardEditorUIService editorUIService) {
        super(ID, NAME);

        plugin.injectLang(RewardComponentLang.class);

        ConfigCodecs.register(StandardRewardEntry.class, RewardEntryCodec.INSTANCE);
        ConfigCodecs.register(StandardRewardsComponent.class, RewardsComponentCodec.INSTANCE);

        RewardComponentEditorService editorService = new RewardComponentEditorService();

        RewardComponentEditorUIService uiService = new RewardComponentEditorUIService(coreUI);
        RewardComponentEditorUIController uiController = new RewardComponentEditorUIController(
            crates, editorUIService, editorService, uiService, dispatcher
        );

        this.addComponent(new RewardComponentEditorDialogRegistrar(coreUI, uiController));
        this.addComponent(new RewardComponentEditorMenuRegistrar(plugin, coreUI, uiController));

        this.crateEditorExtension = new RewardComponentEditorExtension(uiController);
        this.crateDataExtension = new RewardComponentExtension(dataService);
        this.cratePlaceholder = new RewardComponentPlaceholder(rewards);
    }

    public RewardComponentExtension getCrateDataExtension() {
        return this.crateDataExtension;
    }

    public RewardComponentPlaceholder getCratePlaceholder() {
        return this.cratePlaceholder;
    }

    public RewardComponentEditorExtension getCrateEditorExtension() {
        return this.crateEditorExtension;
    }
}
