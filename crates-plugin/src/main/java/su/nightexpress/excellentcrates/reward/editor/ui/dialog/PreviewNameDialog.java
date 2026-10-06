package su.nightexpress.excellentcrates.reward.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.PreviewDialogContext;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class PreviewNameDialog extends Dialog<PreviewDialogContext> {

    private static final String JSON_NAME = "name";

    private final RewardEditorUIController controller;

    public PreviewNameDialog(RewardEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, PreviewDialogContext dialogContext) {
        Identifier rewardId = dialogContext.rewardId();
        RewardPreview preview = dialogContext.preview();

        List<WrappedDialogInput> inputs = new ArrayList<>();

        inputs.add(DialogInputs.text(JSON_NAME, RewardEditorLang.UI_DIALOG_PREVIEW_NAME_INPUT_NAME)
            .initial(preview.getName())
            .maxLength(300)
            .width(300)
            .build());

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardEditorLang.UI_DIALOG_PREVIEW_NAME_TITLE)
                .body(DialogBodies.plain(RewardEditorLang.UI_DIALOG_PREVIEW_NAME_BODY).build())
                .inputs(inputs)
                .build());

            builder.type(DialogTypes.confirmation(DialogButtons.apply(), DialogButtons.cancel()));
            builder.handleResponse(DialogActions.APPLY, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_NAME, preview.getName());

                this.controller.onDialogPreviewNameApplyClick(player, rewardId, name);
                viewer.callback();
            });
        });
    }
}
