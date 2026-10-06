package su.nightexpress.excellentcrates.crates.item.editor.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.item.editor.CrateItemEditorService;
import su.nightexpress.excellentcrates.crates.item.editor.ui.dialog.context.CrateItemIconDialogContext;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context.CrateItemSettingsMenuContext;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context.CrateItemSetupContext;
import su.nightexpress.excellentcrates.util.ItemHelper;

@NullMarked
public class CrateItemEditorUIController implements FeedbackHandler {

    private final CrateItemEditorService   editorService;
    private final CrateItemEditorUIService uiService;
    private final MessageDispatcher        dispatcher;

    public CrateItemEditorUIController(CrateItemEditorService editorService,
                                       CrateItemEditorUIService uiService,
                                       MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player,
                                 Identifier crateId,
                                 CrateEditorHook hook,
                                 BackwardNavigator backwardNavigator) {

        CrateItemSettingsMenuContext menuContext = new CrateItemSettingsMenuContext(crateId, hook, backwardNavigator);

        this.handleFeedback(player, this.uiService.openSettingsMenu(player, menuContext));
    }

    public void onItemMenuIconClick(Player player, CrateItemSettingsMenuContext currentContext, ItemStack selectedItem,
                                    Runnable refreshUI) {
        CrateEditorHook hook = currentContext.hook();

        if (!ItemHelper.isVanillaOnly(selectedItem) || selectedItem.hasItemMeta()) {
            CrateItemIconDialogContext dialogContext = new CrateItemIconDialogContext(currentContext.crateId(),
                selectedItem, hook);
            this.handleFeedback(player, this.uiService.showItemIconDialog(player, dialogContext, refreshUI));
        }
        else {
            CrateItemSetupContext setupContext = new CrateItemSetupContext(hook, selectedItem, false, false, false);
            this.editorService.setItemIcon(hook, currentContext.crateId(), setupContext);
            refreshUI.run();
        }
    }

    public void onItemMenuStackableClick(Player player, CrateItemSettingsMenuContext currentContext, boolean state) {
        Identifier crateId = currentContext.crateId();
        CrateEditorHook hook = currentContext.hook();

        this.editorService.setItemStackable(hook, crateId, state);
    }

    public void onItemMenuUseDisplayClick(Player player, CrateItemSettingsMenuContext currentContext, boolean state) {
        Identifier crateId = currentContext.crateId();
        CrateEditorHook hook = currentContext.hook();

        this.editorService.setItemUseDisplay(hook, crateId, state);
    }

    public void onItemIconDialogApply(Player player, Identifier crateId, CrateItemSetupContext setupContext) {
        CrateEditorHook hook = setupContext.hook();

        this.editorService.setItemIcon(hook, crateId, setupContext);
    }
}
