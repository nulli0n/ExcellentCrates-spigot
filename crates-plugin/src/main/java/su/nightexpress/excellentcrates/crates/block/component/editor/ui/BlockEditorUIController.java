package su.nightexpress.excellentcrates.crates.block.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.block.component.editor.BlockEditorService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context.BlockUnlinkDialogContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockCatalogMenuContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockSettingsMenuContext;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;

@NullMarked
public class BlockEditorUIController implements CrateFeedbackHandler {

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

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onCrateEditorBlockComponentClick(Player player,
                                                 Identifier crateId,
                                                 CrateEditorHook context,
                                                 BackwardNavigator backwardNavigator) {
        BlockSettingsMenuContext menuContext = new BlockSettingsMenuContext(crateId, context, backwardNavigator);

        this.handleFeedback(player, this.uiService.openComponentMenu(player, menuContext));
    }

    public void onComponentMenuBlockCatalogClick(Player player, BlockSettingsMenuContext context) {
        Identifier crateId = context.crateId();

        BackwardNavigator currentNavigator = context.backwardNavigator();
        BackwardNavigator backwardNavigator = user -> {
            CrateEditorHook editorContext = context.editorHook();
            BlockSettingsMenuContext newContext = new BlockSettingsMenuContext(crateId, editorContext,
                currentNavigator);

            this.handleFeedback(player, this.uiService.openComponentMenu(player, newContext));
        };

        BlockCatalogMenuContext menuContext = new BlockCatalogMenuContext(crateId, backwardNavigator);
        this.handleFeedback(player, this.uiService.openCatalogMenu(player, menuContext));
    }

    public void onComponentMenuUnlinkClick(Player player, BlockSettingsMenuContext context, Runnable refreshUI) {
        BlockUnlinkDialogContext dialogContext = new BlockUnlinkDialogContext(context.crateId(), context.editorHook());

        this.handleFeedback(player, this.uiService.showUnlinkConfirmDialog(player, dialogContext, refreshUI));
    }

    public void onCatalogMenuGetBlockClick(Player player, Crate crate, CrateBlock block) {
        this.handleFeedback(player, this.itemService.getBlockItem(player, crate, block));
    }

    public void onUnlinkDialogConfirmClick(Player player, Identifier crateId, CrateEditorHook hook) {
        this.handleFeedback(player, this.editorService.clearPositions(hook, crateId));
    }
}
