package su.nightexpress.excellentcrates.rarity.reward.component.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.controller.RarityComponentEditorDialogRegistrar;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.controller.RarityComponentEditorMenuRegistrar;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.extension.RarityComponentEditorExtension;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIService;
import su.nightexpress.excellentcrates.rarity.reward.component.lang.RarityComponentLang;

@NullMarked
public class RarityComponentEditorBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rarity.component.editor");
    private static final String     NAME = "Rarity Component Editor";

    private final RarityComponentEditorExtension editorExtension;

    public RarityComponentEditorBootstrapContext(CratesPlugin plugin,
                                                 CoreUIService coreUI,
                                                 RewardMessageDispatcher dispatcher,
                                                 RewardRegistry rewardRegistry,
                                                 RarityRegistry rarityRegistry) {
        super(ID, NAME);

        plugin.injectLang(RarityComponentLang.class);

        RarityComponentEditorService editorService = new RarityComponentEditorService();
        RarityComponentEditorUIService uiService = new RarityComponentEditorUIService(coreUI);
        RarityComponentEditorUIController uiController = new RarityComponentEditorUIController(
            rewardRegistry, editorService, uiService, dispatcher
        );

        this.editorExtension = new RarityComponentEditorExtension(uiController);

        this.addComponent(new RarityComponentEditorDialogRegistrar(coreUI, rarityRegistry, uiController));
        this.addComponent(new RarityComponentEditorMenuRegistrar(plugin, coreUI, rarityRegistry, uiController));
    }

    public RarityComponentEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
