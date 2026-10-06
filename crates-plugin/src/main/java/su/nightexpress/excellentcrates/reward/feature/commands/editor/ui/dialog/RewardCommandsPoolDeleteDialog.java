package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.CommandsBundleDeleteDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class RewardCommandsPoolDeleteDialog extends Dialog<CommandsBundleDeleteDialogContext> {

    private final RewardCommandsEditorUIController controller;

    public RewardCommandsPoolDeleteDialog(RewardCommandsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, CommandsBundleDeleteDialogContext context) {
        RewardEditorHook hook = context.hook();

        CommandBundleContext bundleContext = context.bundleContext();
        UUID bundleId = bundleContext.id();

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_DELETE_TITLE)
                .body(DialogBodies.plain(RewardCommandsLang.UI_DIALOG_COMMANDS_BUNDLE_DELETE_BODY)
                    .build()
                )
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (this.controller.onDialogCommandsBundleDeleteConfirm(player, hook, bundleId)) {
                    viewer.callback();
                }
            });
        });
    }

}
