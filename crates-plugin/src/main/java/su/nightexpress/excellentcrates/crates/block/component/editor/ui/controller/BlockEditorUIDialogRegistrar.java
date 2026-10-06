package su.nightexpress.excellentcrates.crates.block.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIKeys;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.dialog.BlockUnlinkConfirmDialog;

@NullMarked
public class BlockEditorUIDialogRegistrar implements StartupComponent {

    private final CoreUIService           coreUI;
    private final BlockEditorUIController controller;

    public BlockEditorUIDialogRegistrar(CoreUIService coreUI, BlockEditorUIController controller) {
        this.coreUI = coreUI;
        this.controller = controller;
    }

    @Override
    public void start() {
        this.coreUI.registerDialog(BlockEditorUIKeys.DIALOG_UNLINK_CONFIRM,
            new BlockUnlinkConfirmDialog(this.controller)
        );
    }
}
