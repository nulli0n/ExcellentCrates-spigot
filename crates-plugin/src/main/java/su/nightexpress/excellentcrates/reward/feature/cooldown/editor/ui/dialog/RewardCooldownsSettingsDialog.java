package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownMode;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.dialog.context.RewardCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.lang.RewardCooldownsLang;
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
import su.nightexpress.nightcore.util.Enums;

@NullMarked
public class RewardCooldownsSettingsDialog extends Dialog<RewardCooldownsSettingsDialogContext> {

    private static final String KEY_ENABLED  = "enabled";
    private static final String KEY_MODE     = "mode";
    private static final String KEY_DURATION = "duration";

    private final RewardCooldownsEditorUIController controller;

    public RewardCooldownsSettingsDialog(RewardCooldownsEditorUIController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardCooldownsSettingsDialogContext context) {
        CooldownType type = context.type();
        CooldownSnapshot snapshot = context.snapshot();

        boolean currentState = snapshot.enabled();
        CooldownMode currentMode = snapshot.mode();
        long currentDuration = snapshot.duration();

        List<WrappedDialogBody> bodies = new ArrayList<>();
        List<WrappedDialogInput> inputs = new ArrayList<>();

        List<WrappedSingleOptionEntry> stateEntries = new ArrayList<>();
        List<WrappedSingleOptionEntry> modeEntries = new ArrayList<>();

        for (boolean state : new boolean[]{true, false}) {
            String localized = CoreLang.STATE_ENABLED_DISALBED.get(state);
            boolean initial = currentState == state;

            stateEntries.add(new WrappedSingleOptionEntry(String.valueOf(state), localized, initial));
        }

        for (CooldownMode mode : CooldownMode.values()) {
            String localized = Lang.COOLDOWN_MODE.getLocalized(mode);
            boolean initial = currentMode == mode;

            modeEntries.add(new WrappedSingleOptionEntry(mode.id(), localized, initial));
        }

        bodies.add(DialogBodies.plain(RewardCooldownsLang.EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_MAIN).build());
        if (type == CooldownType.GLOBAL) {
            bodies.add(DialogBodies.plain(RewardCooldownsLang.EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_GLOBAL).build());
        }
        else if (type == CooldownType.INDIVIDUAL) {
            bodies.add(DialogBodies.plain(RewardCooldownsLang.EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_PLAYER).build());
        }
        bodies.add(DialogBodies.plain(Lang.GENERIC_UI_DIALOG_COOLDOWN_BODY_MODES).build());

        inputs.add(DialogInputs.singleOption(KEY_ENABLED, Lang.GENERIC_UI_DIALOG_COOLDOWN_INPUT_STATE, stateEntries)
            .build()
        );

        inputs.add(DialogInputs.singleOption(KEY_MODE, Lang.GENERIC_UI_DIALOG_COOLDOWN_INPUT_MODE, modeEntries)
            .build()
        );

        inputs.add(DialogInputs.text(KEY_DURATION, Lang.GENERIC_UI_DIALOG_COOLDOWN_INPUT_DURATION)
            .initial(String.valueOf(currentDuration))
            .maxLength(7)
            .build()
        );

        return Dialogs.create(builder -> {
            builder.base(
                DialogBases.builder(RewardCooldownsLang.EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_TITLE)
                    .body(bodies)
                    .inputs(inputs)
                    .build()
            );

            builder.type(DialogTypes.confirmation(DialogButtons.confirm(), DialogButtons.cancel()));

            builder.handleResponse(DialogActions.CONFIRM, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String stateValue = nbtHolder.getText(KEY_ENABLED).orElse(String.valueOf(currentState));
                String modeValue = nbtHolder.getText(KEY_MODE).orElse(currentMode.id());

                boolean newState = Boolean.parseBoolean(stateValue);
                CooldownMode newMode = Enums.parse(modeValue, CooldownMode.class).orElse(currentMode);
                int newDuration = nbtHolder.getInt(KEY_DURATION, (int) currentDuration);

                CooldownSnapshot newSnapshot = new CooldownSnapshot(newState, newMode, newDuration);

                if (this.controller.onCooldownsSettingsDialogSubmit(player, context, newSnapshot)) {
                    viewer.callback();
                }
            });
        });
    }
}
