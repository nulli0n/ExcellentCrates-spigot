package su.nightexpress.excellentcrates.reward.selectable.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.reward.selectable.component.codec.RewardSelectionComponentCodec;
import su.nightexpress.excellentcrates.reward.selectable.component.data.DefaultSelectiveRewardsComponent;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.SelectiveEditorService;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.extension.SelectiveEditorExtension;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.SelectiveEditorUIController;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.SelectiveEditorUIService;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.controller.SelectiveEditorMenuRegistrar;
import su.nightexpress.excellentcrates.reward.selectable.component.extension.SelectiveRewardsDataExtension;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class SelectiveComponentBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.selection.component");
    private static final String     NAME = "Selective Rewards Component";

    private final SelectiveRewardsDataExtension dataExtension;
    private final SelectiveEditorExtension      editorExtension;

    public SelectiveComponentBootstrapContext(CratesPlugin plugin,
                                              CoreUIService coreUI,
                                              CrateMessageDispatcher dispatcher,
                                              CrateRegistry crates) {
        super(ID, NAME);

        ConfigCodecs.register(DefaultSelectiveRewardsComponent.class, RewardSelectionComponentCodec.INSTANCE);

        SelectiveEditorService editorService = new SelectiveEditorService();
        SelectiveEditorUIService editorUIService = new SelectiveEditorUIService(coreUI);
        SelectiveEditorUIController editorUIController = new SelectiveEditorUIController(
            crates, editorService, editorUIService, dispatcher
        );

        this.dataExtension = new SelectiveRewardsDataExtension();
        this.editorExtension = new SelectiveEditorExtension(editorUIController);

        this.addComponent(new SelectiveEditorMenuRegistrar(plugin, coreUI, editorUIController));
    }

    public SelectiveRewardsDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public SelectiveEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
