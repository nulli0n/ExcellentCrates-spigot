package su.nightexpress.excellentcrates.crates.block.component.editor.ui;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.block.component.editor.BlockEditorService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context.BlockUnlinkDialogContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockSettingsMenuContext;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;

@NullMarked
public class BlockEditorUIController {

    private final BlockItemService       itemService;
    private final BlockEditorService     editorService;
    private final BlockEditorUIService   uiService;
    private final CrateMessageDispatcher dispatcher;

    public BlockEditorUIController(BlockItemService itemService,
                                   BlockEditorService editorService,
                                   BlockEditorUIService uiService,
                                   CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onCrateEditorBlockComponentClick(Player player,
                                                 Identifier crateId,
                                                 CrateEditorHook context,
                                                 BackwardNavigator backwardNavigator) {
        BlockSettingsMenuContext menuContext = new BlockSettingsMenuContext(crateId, context, backwardNavigator);

        this.dispatcher.handleFeedback(player, this.uiService.openComponentMenu(player, menuContext));
    }

    public boolean onSettingsMenuAssignClick(Player player, Crate crate, ItemStack itemStack,
                                             Consumer<ItemStack> callback) {
        ActionResult result = this.itemService.assignCrateToBlockItem(crate, itemStack, callback);

        return this.dispatcher.handleFeedbackBase(player, crate, result);
    }

    public void onComponentMenuUnlinkClick(Player player, BlockSettingsMenuContext context, Runnable refreshUI) {
        BlockUnlinkDialogContext dialogContext = new BlockUnlinkDialogContext(context.crateId(), context.editorHook());

        this.dispatcher.handleFeedback(player,
            this.uiService.showUnlinkConfirmDialog(player, dialogContext, refreshUI)
        );
    }

    public void onUnlinkDialogConfirmClick(Player player, Identifier crateId, CrateEditorHook hook) {
        this.dispatcher.handleFeedback(player, this.editorService.clearPositions(hook, crateId));
    }
}
