package su.nightexpress.excellentcrates.crates.block.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context.BlockUnlinkDialogContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockCatalogMenuContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockSettingsMenuContext;

@NullMarked
public class BlockEditorUIService {

    private final CoreUIService coreUI;

    public BlockEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openComponentMenu(Player player, BlockSettingsMenuContext context) {
        return this.coreUI.openMenu(player, BlockEditorUIKeys.MENU_COMPONENT, context);
    }

    public ActionResult openCatalogMenu(Player player, BlockCatalogMenuContext context) {
        return this.coreUI.openMenu(player, BlockEditorUIKeys.MENU_CATALOG, context);
    }

    public ActionResult showUnlinkConfirmDialog(Player player, BlockUnlinkDialogContext context, Runnable callback) {
        return this.coreUI.showDialog(player, BlockEditorUIKeys.DIALOG_UNLINK_CONFIRM, context, callback);
    }
}
