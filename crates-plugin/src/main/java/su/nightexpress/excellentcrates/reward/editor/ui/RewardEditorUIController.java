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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.editor.RewardEditorService;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreateContext;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreateInputContext;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreationContext;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.PreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.excellentcrates.reward.id.RewardIdService;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardEditorUIController {

    private final RewardIdService         idService;
    private final RewardEditorService     editorService;
    private final RewardEditorUIService   uiService;
    private final RewardMessageDispatcher dispatcher;

    public RewardEditorUIController(RewardIdService idService,
                                    RewardEditorService editorService,
                                    RewardEditorUIService uiService,
                                    RewardMessageDispatcher dispatcher) {
        this.idService = idService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public ActionResult onExtensionModify(Player player, Identifier rewardId, Function<Reward, ActionResult> action) {
        return this.editorService.editReward(rewardId, action);
    }

    public void onExtensionMoveBackward(Player player, RewardOptionsMenuContext currentContext) {
        this.dispatcher.handleFeedback(player, this.uiService.openOptionsMenu(player, currentContext));

    }

    public void onBrowseMenuRewardClick(Player player, Reward reward, BackwardNavigator currentNavigator) {
        BackwardNavigator navigator = user -> this.uiService.openBrowseMenu(user, currentNavigator);
        RewardOptionsMenuContext menuContext = new RewardOptionsMenuContext(reward.id(), navigator);

        this.dispatcher.handleFeedback(player, this.uiService.openOptionsMenu(player, menuContext));
    }

    public boolean onBrowseMenuDetailedCreateClick(Player player, ItemStack itemStack, Runnable refreshUI) {
        Identifier id = this.idService.createUniqueRewardId(itemStack).orElse(null);
        String idName = id == null ? null : id.value(); // Back to string, so user can modify it in the dialog UI.
        if (id == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_ID_GENERATION_FAILED, ctx -> ctx
                .with(SharedPlaceholders.ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            );
        }

        RewardCreateContext createContext = new RewardCreateContext(idName, itemStack);

        return this.dispatcher.handleFeedback(player, this.uiService.openCreationDialog(player, createContext,
            refreshUI));
    }

    public boolean onBrowseMenuFastCreateClick(Player player, ItemStack itemStack) {
        Identifier id = this.idService.createUniqueRewardId(itemStack).orElse(null);
        if (id == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_ID_GENERATION_FAILED, ctx -> ctx
                .with(SharedPlaceholders.ITEM, () -> ItemUtil.getNameSerialized(itemStack))
            );
            return false;
        }

        boolean useItemReference = !ItemHelper.isMixedItem(itemStack);
        boolean setItemContent = true;

        RewardCreationContext creationContext = new RewardCreationContext(itemStack, useItemReference, setItemContent);

        return this.dispatcher.handleFeedback(player, this.editorService.createReward(player, id, creationContext));
    }

    public void onOptionsMenuDeleteClick(Player player, RewardOptionsMenuContext currentContext, ItemStack icon) {
        Identifier rewardId = currentContext.rewardId();
        RewardDeletionDialogContext dialogContext = new RewardDeletionDialogContext(rewardId, icon);

        this.dispatcher.handleFeedback(player, this.uiService.showDeletionDialog(player, dialogContext, () -> {
            // Dialog callback will handle the menu navigation after deletion.
            currentContext.moveBackward(player);
        }));
    }

    public void onOptionsMenuPreviewClick(Player player, RewardOptionsMenuContext currentContext) {
        Identifier rewardId = currentContext.rewardId();
        BackwardNavigator navigator = user -> {
            this.dispatcher.handleFeedback(user, this.uiService.openOptionsMenu(user, currentContext));
        };

        RewardPreviewMenuContext menuContext = new RewardPreviewMenuContext(rewardId, navigator);
        this.dispatcher.handleFeedback(player, this.uiService.openPreviewMenu(player, menuContext));
    }

    public void onPreviewMenuNameClick(Player player, Identifier rewardId, RewardPreview preview,
                                       Runnable callback) {
        PreviewDialogContext dialogContext = new PreviewDialogContext(rewardId, preview);
        this.dispatcher.handleFeedback(player, this.uiService.showPreviewNameDialog(player, dialogContext, callback));
    }

    public void onPreviewMenuLoreClick(Player player, Identifier rewardId, RewardPreview preview,
                                       Runnable callback) {
        PreviewDialogContext dialogContext = new PreviewDialogContext(rewardId, preview);
        this.dispatcher.handleFeedback(player, this.uiService.showPreviewLoreDialog(player, dialogContext, callback));
    }

    public boolean onPreviewMenuIconClick(Player player, Identifier rewardId, ItemStack itemStack) {
        return this.dispatcher.handleFeedback(player, this.editorService.setPreviewIcon(rewardId, itemStack));
    }

    public boolean onPreviewMenuAutoResolveClick(Player player, Identifier rewardId, boolean state) {
        return this.dispatcher.handleFeedback(player, this.editorService.setPreviewAutoResolveFromIcon(rewardId,
            state));
    }

    public void onDialogCreationApplyClick(Player player, RewardCreateInputContext context) {
        String name = context.name();
        Identifier id = IdentifierParser.parseSanitized(name).orElse(null);
        if (id == null) {
            this.dispatcher.send(player, RewardEditorLang.CREATION_INVALID_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> name)
            );
            return;
        }

        ItemStack itemStack = context.itemStack();
        boolean useItemReference = context.useItemReference();
        boolean setItemContent = context.setItemContent();

        RewardCreationContext creationContext = new RewardCreationContext(itemStack, useItemReference, setItemContent);

        this.dispatcher.handleFeedback(player, this.editorService.createReward(player, id, creationContext));
    }

    public boolean onDialogDeletionConfirmClick(Player player, Identifier rewardId) {
        // Dialog will handle the callback to move backward in the menu based on the result of this action.
        return this.dispatcher.handleFeedback(player, this.editorService.deleteReward(rewardId));
    }

    public void onDialogPreviewNameApplyClick(Player player, Identifier rewardId, String name) {
        this.dispatcher.handleFeedback(player, this.editorService.setPreviewName(rewardId, name));
    }

    public void onDialogPreviewLoreApplyClick(Player player, Identifier rewardId, List<String> lore) {
        this.dispatcher.handleFeedback(player, this.editorService.setPreviewLore(rewardId, lore));
    }
}
