package su.nightexpress.excellentcrates.reward.editor.ui;

import java.util.List;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;
import su.nightexpress.excellentcrates.reward.editor.RewardEditorService;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreationContext;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardPreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardManualCreationDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardWeightDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.excellentcrates.reward.id.RewardIdService;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardEditorUIController {

    private final RewardRegistry          rewardRegistry;
    private final RewardIdService         idService;
    private final RewardEditorService     editorService;
    private final RewardEditorUIService   uiService;
    private final RewardMessageDispatcher dispatcher;

    public RewardEditorUIController(RewardRegistry rewardRegistry,
                                    RewardIdService idService,
                                    RewardEditorService editorService,
                                    RewardEditorUIService uiService,
                                    RewardMessageDispatcher dispatcher) {
        this.rewardRegistry = rewardRegistry;
        this.idService = idService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public ActionResult onExtensionModify(Player player, RewardId rewardId, Function<Reward, ActionResult> action) {
        return this.editorService.editReward(rewardId, action);
    }

    public void onExtensionMoveBackward(Player player, RewardOptionsMenuContext currentContext) {
        this.dispatcher.handleFeedback(player, this.uiService.openOptionsMenu(player, currentContext));

    }

    public void onBrowseMenuRewardClick(Player player, Reward reward, RewardBrowseMenuContext currentContext) {
        CrateReference crateRef = currentContext.crateRef();
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);

        Crate crate = crateRef.get();
        if (crate == null) return;

        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedbackBase(user, crate, this.uiService.openBrowseMenu(user, currentContext));
        };

        RewardComponentHook hook = currentContext.hook();
        RewardOptionsMenuContext context = new RewardOptionsMenuContext(hook, crateRef, rewardRef, navigator);

        this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openOptionsMenu(player, context));
    }

    public boolean onBrowseMenuManualCreationClick(Player player, RewardBrowseMenuContext currentContext,
                                                   ItemStack itemStack,
                                                   Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        Crate crate = crateRef.get();
        if (crate == null) return false;

        Identifier crateId = crate.id();
        Identifier rewardId = this.idService.createUniqueRewardId(crateId, itemStack).orElse(null);
        String idName = rewardId == null ? null : rewardId.value(); // Back to string, so user can modify it in the dialog UI.
        if (rewardId == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_ID_GENERATION_FAILED, ctx -> ctx
                .with(SharedPlaceholders.ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            );
        }

        RewardComponentHook hook = currentContext.hook();
        RewardManualCreationDialogContext createContext = new RewardManualCreationDialogContext(hook, crateRef, idName,
            itemStack);

        return this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.openCreationDialog(player, createContext, refreshUI)
        );
    }

    public boolean onBrowseMenuFastCreateClick(Player player, RewardComponentHook hook, Crate crate,
                                               ItemStack itemStack) {
        Identifier crateId = crate.id();
        Identifier rewardId = this.idService.createUniqueRewardId(crateId, itemStack).orElse(null);
        if (rewardId == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_ID_GENERATION_FAILED, ctx -> ctx
                .with(SharedPlaceholders.ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            );
            return false;
        }

        boolean useItemReference = !ItemHelper.isMixed(itemStack);
        boolean setItemContent = true;

        RewardId id = new RewardId(crateId, rewardId);
        RewardCreationContext creationContext = new RewardCreationContext(itemStack, useItemReference, setItemContent);

        return this.dispatcher.handleFeedback(player,
            this.editorService.createReward(player, hook, crate, id, creationContext)
        );
    }

    public void onOptionsDeleteClick(Player player, RewardOptionsMenuContext currentContext, NightItem icon,
                                     Runnable callback) {
        RewardComponentHook hook = currentContext.hook();
        CrateReference crateRef = currentContext.crateRef();
        RewardReference rewardRef = currentContext.rewardRef();

        Reward reward = rewardRef.get();
        if (reward == null) return;

        RewardDeletionDialogContext context = new RewardDeletionDialogContext(hook, crateRef, rewardRef, icon);

        this.dispatcher.handleFeedback(player, this.uiService.showDeletionDialog(player, context, () -> {
            // Dialog callback will handle the menu navigation after deletion.
            callback.run();
        }));
    }

    public void onOptionsWeightClick(Player player, RewardOptionsMenuContext currentContext, double currentWeight,
                                     Runnable refreshUI) {
        CrateReference crateRef = currentContext.crateRef();
        //CrateEditorHook hook = currentContext.hook();
        RewardReference rewardRef = currentContext.rewardRef();

        Crate crate = crateRef.get();
        if (crate == null) return;

        RewardWeightDialogContext dialogContext = new RewardWeightDialogContext(
            crateRef, rewardRef, currentWeight
        );

        this.dispatcher.handleFeedbackBase(player, crate,
            this.uiService.showRewardWeightDialog(player, dialogContext, refreshUI)
        );
    }

    public void onOptionsMenuPreviewClick(Player player, RewardOptionsMenuContext currentContext) {
        RewardReference rewardRef = currentContext.rewardRef();
        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedback(user, this.uiService.openOptionsMenu(user, currentContext));
        };

        RewardPreviewMenuContext menuContext = new RewardPreviewMenuContext(rewardRef, navigator);
        this.dispatcher.handleFeedback(player, this.uiService.openPreviewMenu(player, menuContext));
    }

    public void onPreviewMenuNameClick(Player player, Reward reward, RewardPreview preview,
                                       Runnable callback) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RewardPreviewDialogContext dialogContext = new RewardPreviewDialogContext(rewardRef, preview);
        this.dispatcher.handleFeedback(player, this.uiService.showPreviewNameDialog(player, dialogContext, callback));
    }

    public void onPreviewMenuLoreClick(Player player, Reward reward, RewardPreview preview,
                                       Runnable callback) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RewardPreviewDialogContext dialogContext = new RewardPreviewDialogContext(rewardRef, preview);
        this.dispatcher.handleFeedback(player, this.uiService.showPreviewLoreDialog(player, dialogContext, callback));
    }

    public boolean onPreviewMenuIconClick(Player player, Reward reward, ItemStack itemStack) {
        return this.dispatcher.handleFeedback(player,
            this.editorService.setPreviewIcon(reward.id(), itemStack)
        );
    }

    public boolean onPreviewMenuAutoResolveClick(Player player, Reward reward, boolean state) {
        return this.dispatcher.handleFeedback(player,
            this.editorService.setPreviewAutoResolveFromIcon(reward.id(), state)
        );
    }

    public void onManualCreationDialogSubmit(Player player, Crate crate, String name,
                                             RewardComponentHook hook,
                                             RewardCreationContext result) {
        Identifier crateId = crate.id();
        Identifier rewardId = IdentifierParser.parseSanitized(name).orElse(null);
        if (rewardId == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_INVALID_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> name)
            );
            return;
        }

        RewardId id = new RewardId(crateId, rewardId);

        this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.createReward(player, hook, crate, id, result)
        );
    }

    public boolean onDialogDeletionConfirmClick(Player player, RewardComponentHook hook, Crate crate, Reward reward) {
        // Dialog will handle the callback to move backward in the menu based on the result of this action.
        return this.dispatcher.handleFeedback(player, this.editorService.deleteReward(hook, crate, reward.id()));
    }

    public boolean onWeightDialogApply(Player player, RewardWeightDialogContext context, double weight) {
        Crate crate = context.crateRef().get();
        RewardReference rewardRef = context.rewardRef();

        Reward reward = rewardRef.get();
        if (reward == null) return false;

        return crate != null && this.dispatcher.handleFeedbackBase(player, crate,
            this.editorService.setCrateRewardWeight(reward.id(), weight)
        );
    }

    public void onDialogPreviewNameApplyClick(Player player, Reward reward, String name) {
        this.dispatcher.handleFeedback(player, this.editorService.setPreviewName(reward.id(), name));
    }

    public void onDialogPreviewLoreApplyClick(Player player, Reward reward, List<String> lore) {
        this.dispatcher.handleFeedback(player, this.editorService.setPreviewLore(reward.id(), lore));
    }
}
