package su.nightexpress.excellentcrates.reward.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardPreviewDialogContext;
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
public class RewardNameDialog extends Dialog<RewardPreviewDialogContext> {

    private static final String KEY_NAME = "name";

    private final RewardEditorUIController controller;

    public RewardNameDialog(RewardEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardPreviewDialogContext context) {
        RewardPreview preview = context.preview();

        List<WrappedDialogInput> inputs = new ArrayList<>();

        inputs.add(DialogInputs.text(KEY_NAME, RewardEditorLang.UI_DIALOG_PREVIEW_NAME_INPUT_NAME)
            .initial(preview.getName())
            .maxLength(512)
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

                Reward reward = context.rewardRef().get();
                if (reward == null) return;

                String name = nbtHolder.getText(KEY_NAME, preview.getName());

                this.controller.onDialogPreviewNameApplyClick(player, reward, name);
                viewer.callback();
            });
        });
    }
}
