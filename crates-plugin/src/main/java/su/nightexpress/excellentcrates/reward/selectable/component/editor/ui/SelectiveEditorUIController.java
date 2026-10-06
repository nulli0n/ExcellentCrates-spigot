package su.nightexpress.excellentcrates.reward.selectable.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.SelectiveEditorService;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu.context.SelectiveEditorSettingsMenuContext;

@NullMarked
public class SelectiveEditorUIController {

    private final CrateRegistry            crateRegistry;
    private final SelectiveEditorService   editorService;
    private final SelectiveEditorUIService uiService;
    private final CrateMessageDispatcher   dispatcher;

    public SelectiveEditorUIController(CrateRegistry crateRegistry,
                                       SelectiveEditorService editorService,
                                       SelectiveEditorUIService uiService,
                                       CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);

        SelectiveEditorSettingsMenuContext context = new SelectiveEditorSettingsMenuContext(
            crateRef, hook, navigator
        );

        this.uiService.openSettingsMenu(player, context).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, crate, locale);
        });
    }

    public boolean onComponentMenuStateClick(Player player, Crate crate, CrateEditorHook hook, boolean newState) {
        return this.editorService.setComponentState(hook, newState).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, crate, locale);
        });
    }
}
