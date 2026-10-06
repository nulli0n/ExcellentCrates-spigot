package su.nightexpress.excellentcrates.reward.items.editor.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.items.editor.RewardItemsEditorService;
import su.nightexpress.excellentcrates.reward.items.editor.ui.dialog.context.RewardItemAddDialogContext;
import su.nightexpress.excellentcrates.reward.items.editor.ui.menu.context.RewardItemsMenuContext;
import su.nightexpress.excellentcrates.util.ItemHelper;

@NullMarked
public class RewardItemsEditorUIController {

    private final RewardRegistry             rewardRegistry;
    private final RewardItemsEditorService   editorService;
    private final RewardItemsEditorUIService uiService;
    private final RewardMessageDispatcher    dispatcher;

    public RewardItemsEditorUIController(RewardRegistry rewardRegistry,
                                         RewardItemsEditorService editorService,
                                         RewardItemsEditorUIService uiService,
                                         RewardMessageDispatcher dispatcher) {
        this.rewardRegistry = rewardRegistry;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onExtensionClick(Player player, Reward reward, RewardEditorHook hook, BackwardNavigator navigator) {
        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RewardItemsMenuContext itemsMenuContext = new RewardItemsMenuContext(rewardRef, hook, navigator);

        this.uiService.openItemsMenu(player, itemsMenuContext).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, reward, locale, ctx);
        });
    }

    public void onItemsItemClick(Player player, Reward reward, RewardEditorHook hook, ItemStack itemStack,
                                 Runnable refreshUI) {
        if (ItemHelper.isVanillaOnly(itemStack)) {
            this.editorService.addItemContent(hook, itemStack, false).handleFeedback((locale, ctx) -> {
                this.dispatcher.sendBase(player, reward, locale);
            });
            refreshUI.run();
            return;
        }

        RewardReference rewardRef = this.rewardRegistry.createReference(reward);
        RewardItemAddDialogContext dialogContext = new RewardItemAddDialogContext(rewardRef, hook, itemStack);

        this.uiService.showAddItemDialog(player, dialogContext, refreshUI).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, reward, locale);
        });
    }

    public void onItemsItemRemoveClick(Player player, Reward reward, RewardEditorHook hook, int itemIndex,
                                       Runnable refreshUI) {
        boolean result = this.editorService.removeItemContent(hook, itemIndex).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, reward, locale);
        });
        if (result) {
            refreshUI.run();
        }
    }

    public boolean onDialogItemAddConfirm(Player player, Reward reward, RewardEditorHook hook, ItemStack itemStack,
                                          boolean useItemReference) {
        return this.editorService.addItemContent(hook, itemStack, useItemReference).handleFeedback((locale, ctx) -> {
            this.dispatcher.sendBase(player, reward, locale);
        });
    }
}
