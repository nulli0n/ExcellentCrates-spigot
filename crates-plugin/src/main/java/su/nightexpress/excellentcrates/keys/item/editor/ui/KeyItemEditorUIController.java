package su.nightexpress.excellentcrates.keys.item.editor.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.item.editor.KeyItemEditorService;
import su.nightexpress.excellentcrates.keys.item.editor.ui.context.KeyItemDialogContext;
import su.nightexpress.excellentcrates.keys.item.editor.ui.context.KeyItemSetupContext;
import su.nightexpress.excellentcrates.keys.item.editor.ui.menu.context.KeyItemMainMenuContext;
import su.nightexpress.excellentcrates.util.ItemHelper;

@NullMarked
public class KeyItemEditorUIController implements FeedbackHandler {

    private final KeyItemEditorService   editorService;
    private final KeyItemEditorUIService uiService;
    private final MessageDispatcher      dispatcher;

    public KeyItemEditorUIController(KeyItemEditorService editorService,
                                     KeyItemEditorUIService uiService,
                                     MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier keyId, KeyEditorHook hook, BackwardNavigator backNavigator) {
        KeyItemMainMenuContext context = new KeyItemMainMenuContext(keyId, hook, backNavigator);

        this.handleFeedback(player, this.uiService.openItemMenu(player, context));
    }

    public void onMainMenuIconClick(Player player, KeyItemMainMenuContext currentContext, ItemStack selectedItem,
                                    Runnable refreshUI) {
        Identifier keyId = currentContext.keyId();
        KeyEditorHook hook = currentContext.hook();

        if (!ItemHelper.isBukkitOnly(selectedItem) || selectedItem.hasItemMeta()) {
            KeyItemDialogContext dialogContext = new KeyItemDialogContext(keyId, hook, selectedItem);
            this.handleFeedback(player, this.uiService.showItemIconDialog(player, dialogContext, refreshUI));
        }
        else {
            KeyItemSetupContext setupContext = new KeyItemSetupContext(selectedItem, false, false, false);
            this.handleFeedback(player, this.editorService.setItemIcon(hook, setupContext));
            refreshUI.run();
        }
    }

    public void onMainMenuStackableClick(Player player, KeyItemMainMenuContext currentContext, boolean state) {
        KeyEditorHook hook = currentContext.hook();

        this.handleFeedback(player, this.editorService.setItemStackable(hook, state));
    }

    public boolean onSettingsInheritDisplayClick(Player player, KeyEditorHook hook, boolean state) {
        return this.handleFeedback(player, this.editorService.setInheritDisplaySettings(hook, state));
    }

    public void onItemStackDialogSubmit(Player player, KeyEditorHook hook, KeyItemSetupContext setupContext) {
        this.handleFeedback(player, this.editorService.setItemIcon(hook, setupContext));
    }
}
