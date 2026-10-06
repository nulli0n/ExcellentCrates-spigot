package su.nightexpress.excellentcrates.keys.cost.editor.ui;

import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.keys.cost.editor.KeyCostEditorService;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostAddEntryDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryAmountDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.dialog.context.KeyCostEntryRemoveDialogContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntriesMenuContext;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntryMenuContext;

@NullMarked
public class KeyCostEditorUIController {

    private final CrateRegistry          crateRegistry;
    private final KeyCostEditorService   editorService;
    private final KeyCostEditorUIService uiService;
    private final CrateMessageDispatcher dispatcher;

    public KeyCostEditorUIController(CrateRegistry crateRegistry,
                                     KeyCostEditorService editorService,
                                     KeyCostEditorUIService uiService,
                                     CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        KeyCostEntriesMenuContext context = new KeyCostEntriesMenuContext(crateRef, hook, navigator);

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openEntriesMenu(player, context));
    }

    public void onEntriesMenuEntryClick(Player player,
                                        Crate crate,
                                        KeyCostEntriesMenuContext currentContext,
                                        Identifier keyId) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();

        BackwardNavigator backwardNavigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate, this.uiService.openEntriesMenu(user, currentContext));
        };

        KeyCostEntryMenuContext menuContext = new KeyCostEntryMenuContext(
            hook, crateRef, keyId, backwardNavigator
        );

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openEntryMenu(player, menuContext));
    }

    public void onEntriesMenuAddClick(Player player, Crate crate, CrateEditorHook hook, Set<Identifier> existingKeys,
                                      Runnable refreshUI) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        KeyCostAddEntryDialogContext context = new KeyCostAddEntryDialogContext(crateRef, hook, existingKeys);

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showAddEntryDialog(player, context, refreshUI)
        );
    }

    public boolean onEntriesMenuCostEnabledClick(Player player, Crate crate, CrateEditorHook hook, boolean state) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setCostEnabled(hook, state)
        );
    }

    public void onEntryMenuAmountClick(Player player, Crate crate, CrateEditorHook hook,
                                       Identifier keyId,
                                       int currentAmount,
                                       Runnable refreshUI) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        KeyCostEntryAmountDialogContext dialogContext = new KeyCostEntryAmountDialogContext(
            crateRef, hook, keyId, currentAmount
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showEntryAmountDialog(player, dialogContext, refreshUI)
        );
    }

    public void onEntryMenuRemoveClick(Player player, Crate crate, KeyCostEntryMenuContext menuContext) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        KeyCostEntryRemoveDialogContext dialogContext = new KeyCostEntryRemoveDialogContext(
            crateRef, menuContext.hook(), menuContext.keyId()
        );

        Runnable callback = () -> {
            menuContext.moveBackward(player);
        };

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showRemoveEntryDialog(player, dialogContext, callback)
        );
    }

    public boolean onEntryAmountDialogSubmit(Player player, Crate crate, CrateEditorHook hook, Identifier keyId,
                                             int amount) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setCostEntryAmount(hook, keyId, amount)
        );
    }

    public boolean onEntryAddDialogSubmit(Player player, Crate crate, CrateEditorHook hook, Identifier keyId) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.addCostEntry(hook, keyId)
        );
    }

    public boolean onEntryRemoveDialogSubmit(Player player, Crate crate, CrateEditorHook hook, Identifier keyId) {
        return this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.removeCostEntry(hook, keyId)
        );
    }
}
