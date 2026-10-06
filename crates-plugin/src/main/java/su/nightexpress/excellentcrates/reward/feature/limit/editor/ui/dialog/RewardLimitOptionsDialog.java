package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.limit.LimitSnapshot;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.dialog.context.RewardLimitOptionsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class RewardLimitOptionsDialog extends Dialog<RewardLimitOptionsDialogContext> {

    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_AMOUNT  = "amount";

    private final RewardLimitsEditorUIController controller;

    public RewardLimitOptionsDialog(RewardLimitsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardLimitOptionsDialogContext context) {
        LimitType type = context.type();
        LimitSnapshot snapshot = context.snapshot();
        RewardEditorHook hook = context.hook();

        List<WrappedDialogBody> bodies = new ArrayList<>();
        List<WrappedDialogInput> inputs = new ArrayList<>();

        bodies.add(DialogBodies.plain(RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_MAIN).build());
        if (type == LimitType.GLOBAL) {
            bodies.add(DialogBodies.plain(RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_GLOBAL).build());
        }
        else {
            bodies.add(DialogBodies.plain(RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_PLAYER).build());
        }
        bodies.add(DialogBodies.plain(RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_ALTERNATIVE).build());

        List<WrappedSingleOptionEntry> options = new ArrayList<>();
        for (boolean state : new boolean[]{true, false}) {
            String key = String.valueOf(state);
            String localized = CoreLang.STATE_ENABLED_DISALBED.get(state);
            boolean initial = snapshot.enabled() == state;

            options.add(new WrappedSingleOptionEntry(key, localized, initial));
        }

        inputs.add(
            DialogInputs.singleOption(KEY_ENABLED, RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_INPUT_ENABLED,
                options).build()
        );

        inputs.add(
            DialogInputs.text(KEY_AMOUNT, RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_INPUT_AMOUNT)
                .initial(String.valueOf(snapshot.amount()))
                .maxLength(6)
                .build()
        );

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardLimitsLang.EDITOR_UI_DIALOG_LIMIT_OPTIONS_TITLE)
                .body(bodies)
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String rawState = nbtHolder.getText(KEY_ENABLED, String.valueOf(snapshot.enabled()));
                boolean newState = Boolean.parseBoolean(rawState);

                int amount = nbtHolder.getInt(KEY_AMOUNT, snapshot.amount());

                LimitSnapshot newSnapshot = new LimitSnapshot(newState, amount);

                if (this.controller.onLimitOptionsDialogConfirm(player, type, newSnapshot, hook)) {
                    viewer.callback();
                }
            });
        });
    }

}
