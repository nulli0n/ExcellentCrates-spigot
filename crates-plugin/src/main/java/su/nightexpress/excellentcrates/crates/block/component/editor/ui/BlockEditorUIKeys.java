package su.nightexpress.excellentcrates.crates.block.component.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.context.BlockUnlinkDialogContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockCatalogMenuContext;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockSettingsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class BlockEditorUIKeys {

    public static final MenuKey<BlockCatalogMenuContext> MENU_CATALOG = MenuKey.of(
        "crate.blocks.editor.catalog"
    );

    public static final MenuKey<BlockSettingsMenuContext> MENU_COMPONENT = MenuKey.of(
        "crate.blocks.editor.component"
    );

    public static final DialogKey<BlockUnlinkDialogContext> DIALOG_UNLINK_CONFIRM = new DialogKey<>(
        "crate.blocks.unlink_confirm"
    );

    private BlockEditorUIKeys() {
    }
}
