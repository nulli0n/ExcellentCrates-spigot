package su.nightexpress.excellentcrates.reward.crate.component.editor.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentEditorService;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.dialog.context.RewardRollCountDialogContext;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.context.RewardComponentSettingsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIService;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardBrowseMenuContext;

@NullMarked
public class RewardComponentEditorUIController {

    private final CrateRegistry                  crateRegistry;
    private final RewardEditorUIService          editorUIService;
    private final RewardComponentEditorService   editorService;
    private final RewardComponentEditorUIService uiService;
    private final CrateMessageDispatcher         dispatcher;

    public RewardComponentEditorUIController(CrateRegistry crateRegistry,
                                             RewardEditorUIService editorUIService,
                                             RewardComponentEditorService editorService,
                                             RewardComponentEditorUIService uiService,
                                             CrateMessageDispatcher dispatcher) {
        this.crateRegistry = crateRegistry;
        this.editorUIService = editorUIService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        CrateReference crateRef = this.crateRegistry.createReference(crate);
        RewardComponentSettingsMenuContext menuContext = new RewardComponentSettingsMenuContext(
            hook, crateRef, navigator
        );

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openRewardsMenu(player, menuContext));
    }

    public void onSettingsRewardsClick(Player player, RewardComponentSettingsMenuContext currentContext) {
        CrateReference crateRef = currentContext.crateRef();
        Crate crate = crateRef.get();
        if (crate == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate, this.uiService.openRewardsMenu(user, currentContext));
        };

        RewardComponentHook hook = new RewardComponentHook(currentContext.hook(), editorService);
        RewardBrowseMenuContext menuContext = new RewardBrowseMenuContext(new AtomicBoolean(), hook, crateRef,
            navigator);

        this.dispatcher.handleFeedbackBase(player, crate, this.editorUIService.openBrowseMenu(player, menuContext));
    }

    public void onSettingsRollCountClick(Player player, RewardComponentSettingsMenuContext currentContext,
                                         int currentAmount,
                                         Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        CrateEditorHook hook = currentContext.hook();

        Crate crate = crateRef.get();
        if (crate == null) return;

        RewardRollCountDialogContext context = new RewardRollCountDialogContext(hook, crateRef, currentAmount);

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showRewardsAmountDialog(player, context, refreshUI)
        );
    }

    public boolean onRollCountDialogSubmit(Player player, RewardRollCountDialogContext context, int rollCount) {
        Crate crate = context.crateRef().get();
        CrateEditorHook hook = context.hook();

        return crate != null && this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setRequiredAmount(hook, rollCount)
        );
    }
}
